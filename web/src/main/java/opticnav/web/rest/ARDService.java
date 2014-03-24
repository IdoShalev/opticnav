package opticnav.web.rest;

import javax.servlet.http.HttpServletResponse;

import opticnav.ardd.admin.AdminConnection;
import opticnav.ardd.protocol.ConfCode;
import opticnav.persistence.web.AccountBroker;
import opticnav.web.arddbrokerpool.ARDdAdminPool;
import opticnav.web.components.UserSession;
import opticnav.web.rest.pojo.Message;
import opticnav.web.util.InputUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ard")
public class ARDService extends Controller {
    @Autowired
    private javax.sql.DataSource dbDataSource;
    
    @Autowired
    private UserSession userSession;
    
    @Autowired
    private ARDdAdminPool pool;
    
    @RequestMapping(method=RequestMethod.POST)
    public Message register(@RequestBody String confirmationCode, HttpServletResponse resp)
            throws Exception {
        try (AccountBroker ab = new AccountBroker(dbDataSource, userSession.getUser().getId())) {
            if (ab.hasARD()) {
                throw new Conflict("registerard.existing");
            }
            
            if (InputUtil.isEntered(confirmationCode)) {
                if (ConfCode.isStringCodeValid(confirmationCode)) {
                    ConfCode code = new ConfCode(confirmationCode);
                    
                    try (AdminConnection b = this.pool.getAdminBroker()) {
                        int ardID = b.registerARDWithConfCode(code);
                        boolean successful = ardID != 0;
                        
                        if (successful) {
                            ab.setARD(ardID);
                            
                            return ok("registerard.successful");
                        } else {
                            throw new NotFound("registerard.nomatch");
                        }
                    }
                } else {
                    throw new BadRequest("registerard.badcode");
                }
            } else {
                throw new BadRequest("registerard.nocode");
            }
        }
    }
    
    @RequestMapping(method=RequestMethod.GET)
    @ResponseBody
    public Message getRegisteredARD() throws Exception {
        boolean hasRegistered;
        
        try (AccountBroker b = new AccountBroker(dbDataSource, userSession.getUser().getId())) {
            hasRegistered = b.hasARD();
        }
        
        if (hasRegistered) {
            return ok("ard.hasregistered");
        } else {
            throw new NotFound("ard.noregistered");
        }
    }
    
    @RequestMapping(method=RequestMethod.DELETE)
    @ResponseBody
    public Message unregisterARD() throws Exception {
        try (AccountBroker b = new AccountBroker(dbDataSource, userSession.getUser().getId())) {
            b.removeARD();
        }
        
        return ok("ard.unregistered");
    }
}
