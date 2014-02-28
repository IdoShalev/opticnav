package opticnav.web.rest;

import javax.servlet.http.HttpServletResponse;

import opticnav.ardd.admin.AdminConnection;
import opticnav.ardd.protocol.HexCode;
import opticnav.ardd.protocol.Protocol;
import opticnav.web.arddbrokerpool.ARDdAdminPool;
import opticnav.web.rest.pojo.ARD;
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
            if (HexCode.isStringCodeValid(confirmationCode, Protocol.CONFCODE_BYTES)) {
                HexCode code = new HexCode(confirmationCode);
                
                try (AdminConnection b = this.pool.getAdminBroker()) {
                    boolean successful = b.registerARDWithConfCode(code) != 0;
                    
                    if (successful) {
                        return ok("registerard.successful");
                    } else {
                        return badRequest("registerard.nomatch");
                    }
                }
            } else {
                return badRequest("registerard.badcode");
            }
        } else {
            return badRequest("registerard.nocode");
        }
    }
    
    @RequestMapping(method=RequestMethod.GET)
    @ResponseBody
    public ARD info() {
        return new ARD(44, "Tacocat");
    }
}
