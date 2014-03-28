package opticnav.web.controllers.rest;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import opticnav.persistence.web.WebAccountDAO;
import opticnav.persistence.web.WebResourceDAO;
import opticnav.persistence.web.map.Anchor;
import opticnav.persistence.web.map.GetMap;
import opticnav.persistence.web.map.MapsListEntry;
import opticnav.persistence.web.map.Marker;
import opticnav.persistence.web.map.ModifyMap;
import opticnav.web.components.UserSession;
import opticnav.web.controllers.rest.pojo.Message;
import opticnav.web.controllers.rest.pojo.map.*;
import opticnav.web.util.InputUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/map")
public class MapController extends Controller {
    @Autowired
    private javax.sql.DataSource dbDataSource;
    
    @Autowired
    private UserSession userSession;
    
    @Value("${opticnav.resource.dir}")
    private String resourcePath;
    
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public GetMapPOJO getMap(@PathVariable("id") int id) throws Exception {
        try (WebAccountDAO dao = new WebAccountDAO(dbDataSource, userSession.getUser().getId())) {
            GetMap map = dao.getMap(id);
            return new GetMapPOJO(map);
        }
    }
    
    @RequestMapping(method=RequestMethod.GET)
    public List<GetMapsListEntryPOJO> getMapsList() throws Exception {
        try (WebAccountDAO dao = new WebAccountDAO(dbDataSource, userSession.getUser().getId())) {
            List<MapsListEntry> mapsList = dao.getMapsList();
            List<GetMapsListEntryPOJO> list = new LinkedList<>();
            
            for (MapsListEntry entry: mapsList) {
                GetMapsListEntryPOJO item = new GetMapsListEntryPOJO();
                item.name = entry.getName();
                item.id = entry.getId();
                list.add(item);
            }
            
            return list;
        }
    }
    
    @RequestMapping(method=RequestMethod.POST,
            headers="content-type=multipart/form-data")
    public CreateMapPOJO createMap(HttpServletRequest request, @RequestParam String name)
            throws Exception {
        int imageResourceID, mapID;
        
        if (!InputUtil.isEntered(name)) {
            throw new BadRequest("map.noname");
        }
        
        try (WebResourceDAO dao = new WebResourceDAO(resourcePath, dbDataSource)) {
            imageResourceID = ResourceUploadUtil.upload(this, dao, request);
        }
        try (WebAccountDAO dao = new WebAccountDAO(dbDataSource, userSession.getUser().getId())) {
            mapID = dao.createMap(name, imageResourceID);
        }
        
        return new CreateMapPOJO(mapID);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public Message modifyMap(@PathVariable("id") int id,
            @RequestBody ModifyMapPOJO map) throws Exception {
        try (WebAccountDAO dao = new WebAccountDAO(dbDataSource, userSession.getUser().getId())) {
            ModifyMap pMap = new ModifyMap();
            for (MarkerPOJO m: map.markers) {
                // TODO - resource
                Marker marker = new Marker(m.gps.lng, m.gps.lat, m.info.name, 0);
                pMap.addMarker(marker);
            }
            for (AnchorPOJO a: map.anchors) {
                Anchor anchor = new Anchor(a.gps.lng, a.gps.lat, a.local.x, a.local.y);
                pMap.addAnchor(anchor);
            }
            dao.modifyMap(id, pMap);
            
            return ok("map.saved");
        }
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public Message deleteMap(@PathVariable("id") int id) throws Exception {
        try (WebAccountDAO dao = new WebAccountDAO(dbDataSource, userSession.getUser().getId())) {
            dao.deleteMap(id);
            return ok("map.deleted");
        }
    }
}
