package opticnav.web.rest;

import opticnav.persistence.web.AccountBroker;
import opticnav.persistence.web.Anchor;
import opticnav.persistence.web.Map;
import opticnav.persistence.web.Marker;
import opticnav.web.components.UserSession;
import opticnav.web.rest.pojo.map.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/map")
public class MapService extends Controller {
    @Autowired
    private javax.sql.DataSource dbDataSource;
    
    @Autowired
    private UserSession userSession;
    
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public GetMapPOJO getMap(@PathVariable("id") int id) throws Exception {
        try (AccountBroker broker = new AccountBroker(dbDataSource, userSession.getUser().getId())) {
            Map map = broker.getMap(id);
            return new GetMapPOJO(map);
        }
    }
    
    @RequestMapping(method=RequestMethod.POST)
    public void createMap(@RequestBody CreateMapPOJO map) throws Exception {
        try (AccountBroker broker = new AccountBroker(dbDataSource, userSession.getUser().getId())) {
            broker.createMap(map.name, map.image_resource);
        }
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public void modifyMap(@PathVariable("id") int id,
            @RequestBody ModifyMapPOJO map) throws Exception {
        try (AccountBroker broker = new AccountBroker(dbDataSource, userSession.getUser().getId())) {
            Map pMap = new Map();
            for (MarkerPOJO m: map.markers) {
                // TODO - resource
                Marker marker = new Marker(m.lng, m.lat, m.name, 0);
                pMap.addMarker(marker);
            }
            for (AnchorPOJO a: map.anchors) {
                Anchor anchor = new Anchor(a.lng, a.lat, a.localX, a.localY);
                pMap.addAnchor(anchor);
            }
            broker.modifyMap(id, pMap);
        }
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public void deleteMap(@PathVariable("id") int id) throws Exception {
        try (AccountBroker broker = new AccountBroker(dbDataSource, userSession.getUser().getId())) {
            broker.deleteMap(id);
        }
    }
}
