package opticnav.web.rest;

import javax.servlet.http.HttpServletResponse;

import opticnav.ardd.admin.AdminConnection;
import opticnav.ardd.protocol.ConfCode;
import opticnav.web.arddbrokerpool.ARDdAdminPool;
import opticnav.web.rest.pojo.Message;
import opticnav.web.util.InputUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/ard/**")
public class ARDService extends Controller {
    @Autowired
    private ARDdAdminPool pool;
    
    @RequestMapping(method=RequestMethod.POST)
    public Message register(@RequestBody String confirmationCode, HttpServletResponse resp)
            throws Exception {
        if (InputUtil.isEntered(confirmationCode)) {
            if (ConfCode.isStringCodeValid(confirmationCode)) {
                ConfCode code = new ConfCode(confirmationCode);
                
                try (AdminConnection b = this.pool.getAdminBroker()) {
                    boolean successful = b.registerARDWithConfCode(code) != 0;
                    
                    if (successful) {
                        return ok("registerard.successful");
                    } else {
                        throw new BadRequest("registerard.nomatch");
                    }
                }
            } else {
                throw new BadRequest("registerard.badcode");
            }
        } else {
            throw new BadRequest("registerard.nocode");
        }
    }
    
    @RequestMapping(method=RequestMethod.GET)
    @ResponseBody
    public Message getRegisteredARD() throws Exception {
        boolean hasRegistered = false;
        
        if (hasRegistered) {
            return ok("ard.hasregistered");
        } else {
            throw new NotFound("ard.noregistered");
        }
    }
    
    @RequestMapping(method=RequestMethod.DELETE)
    @ResponseBody
    public Message removeARD() {
        boolean hasRegistered = false;
        return ok(hasRegistered ? "ard.hasregistered" : "ard.noregistered");
    }
}
