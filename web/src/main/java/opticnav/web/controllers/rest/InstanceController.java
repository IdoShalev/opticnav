package opticnav.web.controllers.rest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimeType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import opticnav.daemon.manager.ARDdAdmin;
import opticnav.daemon.manager.ARDdAdminStartInstanceStatus;
import opticnav.daemon.manager.InstanceDeployment;
import opticnav.daemon.manager.InstanceDeploymentBuilder;
import opticnav.daemon.protocol.GeoCoordFine;
import opticnav.daemon.protocol.InstanceDeploymentInfo;
import opticnav.persistence.WebAccountDAO;
import opticnav.persistence.Resource;
import opticnav.persistence.WebResourceDAO;
import opticnav.persistence.map.Anchor;
import opticnav.persistence.map.GetMap;
import opticnav.persistence.map.Marker;
import opticnav.web.arddbrokerpool.ARDdAdminPool;
import opticnav.web.components.UserSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/instance")
public class InstanceController extends Controller {
    public static class StartInstancePOJO {
        public int map_id;
        @NotNull
        public List<Integer> accounts;
    }
    
    public static class InstanceIDPOJO {
        public int instance_id;
    }
    
    public static class InstanceInfoPOJO {
        public static class ARD {
            public int ard_id;
            public String name;
        }
        
        public int instance_id;
        public String name;
        public long start_time;
        public List<ARD> ards;
    }
    
    @Autowired
    private javax.sql.DataSource dbDataSource;
    
    @Autowired
    private UserSession userSession;
    
    @Value("${opticnav.resource.dir}")
    private String resourcePath;
    
    @Autowired
    private ARDdAdminPool pool;
    
    @RequestMapping(method=RequestMethod.GET)
    public List<InstanceInfoPOJO> getCurrentInstances() throws Exception {
        final List<InstanceDeploymentInfo> instances;
        try (ARDdAdmin broker = this.pool.getAdminBroker()) {
            final long owner = userSession.getUser().getId();
            instances = broker.listInstancesByOwner(owner);
        }
            
        final List<InstanceInfoPOJO> list = new ArrayList<>(instances.size());
        for (InstanceDeploymentInfo info: instances) {
            final InstanceInfoPOJO pojo = new InstanceInfoPOJO();
            pojo.instance_id = info.getId();
            pojo.name = info.getName();
            pojo.start_time = info.getStartTime();
            pojo.ards = new ArrayList<>(info.getArds().size());
            
            for (InstanceDeploymentInfo.ARDIdentifier ard: info.getArds()) {
                final InstanceInfoPOJO.ARD ardPOJO = new InstanceInfoPOJO.ARD();
                ardPOJO.ard_id = ard.getId();
                ardPOJO.name = ard.getName();
                
                pojo.ards.add(ardPOJO);
            }
            
            list.add(pojo);
        }
        return list;
    }
    
    @RequestMapping(method=RequestMethod.POST)
    public InstanceIDPOJO startInstance(@RequestBody @Valid StartInstancePOJO startInstancePOJO)
            throws Exception {
        final long owner;
        final String mapName;
        final MimeType mapImageType;
        final int mapImageSize;
        final InputStream mapImageInput;
        final List<InstanceDeployment.Marker> mapMarkers;
        final List<InstanceDeployment.Anchor> mapAnchors;
        final List<InstanceDeployment.ARDIdentifier> ardList;

        owner = userSession.getUser().getId();
        ardList = new ArrayList<>(startInstancePOJO.accounts.size());
        
        // Add the ARD of every user provided
        for (int account_id: startInstancePOJO.accounts) {
            try (WebAccountDAO dao = new WebAccountDAO(dbDataSource, account_id)) {
                ardList.add(new InstanceDeployment.ARDIdentifier(dao.getARD(), dao.getUsername()));
            }
        }
        
        try (WebAccountDAO dao = new WebAccountDAO(dbDataSource, userSession.getUser().getId())) {
            // Add the user's ARD (if they have one)
            if (dao.hasARD()) {
                ardList.add(new InstanceDeployment.ARDIdentifier(dao.getARD(), dao.getUsername()));
            }
            
            final GetMap map = dao.getMap(startInstancePOJO.map_id);
            if (map == null) {
                // map not found
                throw new NotFound("instance.mapnotfound", startInstancePOJO.map_id);
            }
            int imageResourceID = map.getImageResource();
            
            mapName = map.getName();
            
            // We need to re-add markers as InstanceDeployment.Marker objects because they're in separate layers
            mapMarkers = new ArrayList<>(map.getMarkers().size());
            for (Marker marker: map.getMarkers()) {
                final InstanceDeployment.Marker instMarker;
                instMarker = new InstanceDeployment.Marker(marker.getName(), marker.getLng()*32, marker.getLat()*32);
                mapMarkers.add(instMarker);
            }

            // We need to re-add anchors as InstanceDeployment.Anchor objects because they're in separate layers
            mapAnchors = new ArrayList<>(map.getAnchors().size());
            for (Anchor anchor: map.getAnchors()) {
                final GeoCoordFine geoCoord;
                geoCoord = new GeoCoordFine(anchor.getLng()*32, anchor.getLat()*32);
                
                final InstanceDeployment.Anchor instAnchor;
                instAnchor = new InstanceDeployment.Anchor(geoCoord,
                                                           anchor.getLocalX(), anchor.getLocalY());
                mapAnchors.add(instAnchor);
            }
            
            try (WebResourceDAO resDao = new WebResourceDAO(resourcePath, dbDataSource)) {
                final Resource res = resDao.getResource(imageResourceID);
                mapImageType = res.getMimeType();
                mapImageSize = res.getSize();
                mapImageInput = res.getInputStream();
            }
        }
        
        final ARDdAdminStartInstanceStatus status;
        final InstanceDeployment deployment = new InstanceDeploymentBuilder()
                                                  .setMapName(mapName)
                                                  .setMapImage(mapImageType, mapImageSize, mapImageInput, mapAnchors)
                                                  .setMapMarkers(mapMarkers)
                                                  .setARDList(ardList)
                                                  .build();
        
        try (ARDdAdmin broker = this.pool.getAdminBroker()) {
            status = broker.deployInstance(owner, deployment);
        }
        
        if (status.getStatus() == ARDdAdminStartInstanceStatus.Status.DEPLOYED) {
            InstanceIDPOJO pojo = new InstanceIDPOJO();
            pojo.instance_id = status.getInstanceID();
            return pojo;
        } else {
            // XXX - should throw a not-an-Exception exception, maybe
            throw new Exception("Could not start instance: " + status.getStatus());
        }
    }
    
    @RequestMapping(method=RequestMethod.DELETE)
    public void stopInstance() throws Exception {
        final long owner;
        owner = userSession.getUser().getId();
        
        try (ARDdAdmin broker = this.pool.getAdminBroker()) {
            final List<InstanceDeploymentInfo> instances = broker.listInstancesByOwner(owner);
            
            for (InstanceDeploymentInfo i: instances) {
                broker.stopInstance(i.getId());
            }
        }
    }
}
