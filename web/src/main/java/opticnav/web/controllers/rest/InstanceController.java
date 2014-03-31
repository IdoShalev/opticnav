package opticnav.web.controllers.rest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimeType;

import opticnav.ardd.admin.ARDdAdmin;
import opticnav.ardd.admin.ARDdAdminStartInstanceStatus;
import opticnav.ardd.admin.InstanceDeployment;
import opticnav.ardd.admin.InstanceDeploymentBuilder;
import opticnav.persistence.web.WebAccountDAO;
import opticnav.persistence.web.Resource;
import opticnav.persistence.web.WebResourceDAO;
import opticnav.persistence.web.map.Anchor;
import opticnav.persistence.web.map.GetMap;
import opticnav.persistence.web.map.Marker;
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
        public List<Integer> accounts;
    }
    
    public static class InstanceIDPOJO {
        public int instance_id;
    }
    
    @Autowired
    private javax.sql.DataSource dbDataSource;
    
    @Autowired
    private UserSession userSession;
    
    @Value("${opticnav.resource.dir}")
    private String resourcePath;
    
    @Autowired
    private ARDdAdminPool pool;
    
    @RequestMapping(method=RequestMethod.POST)
    public InstanceIDPOJO startInstance(@RequestBody StartInstancePOJO startInstancePOJO)
            throws Exception {
        final String mapName;
        final MimeType mapImageType;
        final int mapImageSize;
        final InputStream mapImageInput;
        final List<InstanceDeployment.Marker> mapMarkers;
        final List<InstanceDeployment.Anchor> mapAnchors;
        final List<InstanceDeployment.ARDIdentifier> ardList;
        
        mapMarkers = new ArrayList<>();
        mapAnchors = new ArrayList<>();
        ardList   = new ArrayList<>();
        
        for (int account_id: startInstancePOJO.accounts) {
            try (WebAccountDAO dao = new WebAccountDAO(dbDataSource, account_id)) {
                ardList.add(new InstanceDeployment.ARDIdentifier(dao.getARD(), dao.getUsername()));
            }
        }
        
        try (WebAccountDAO dao = new WebAccountDAO(dbDataSource, userSession.getUser().getId())) {
            final GetMap map = dao.getMap(startInstancePOJO.map_id);
            int imageResourceID = map.getImageResource();
            
            mapName = map.getName();
            
            // We need to re-add markers as InstanceDeployment.Marker objects because they're in separate layers
            for (Marker marker: map.getMarkers()) {
                final InstanceDeployment.Marker instMarker;
                instMarker = new InstanceDeployment.Marker(marker.getName(), marker.getLng(), marker.getLat());
                mapMarkers.add(instMarker);
            }

            // We need to re-add anchors as InstanceDeployment.Anchor objects because they're in separate layers
            for (Anchor anchor: map.getAnchors()) {
                final InstanceDeployment.Anchor instAnchor;
                instAnchor = new InstanceDeployment.Anchor(anchor.getLng(), anchor.getLat(),
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
            status = broker.deployInstance(deployment);
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
}
