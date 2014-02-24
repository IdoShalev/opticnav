package opticnav.web.rest;

import javax.servlet.http.HttpServletResponse;

import opticnav.web.rest.pojo.Account;
import opticnav.web.rest.pojo.Message;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/account/**")
public class AccountService extends Controller {
    @RequestMapping(value="login", method=RequestMethod.POST)
    public Message login(@RequestBody Account account, HttpServletResponse resp) {
        if ("Derpy".equals(account.username) && "Hooves".equals(account.password)) {
            return ok("account.loggedin");
        } else {
            return badRequest("account.couldnotlogin");
        }
    }
    
    @RequestMapping(value="logout", method=RequestMethod.POST)
    public Message logout(HttpServletResponse resp) {
        return ok("account.loggedout");
    }
    
    @RequestMapping(value="register", method=RequestMethod.POST)
    public Message register(@RequestBody Account account, HttpServletResponse resp) {
        if ("Derpy".equals(account.username)) {
            return badRequest("account.couldnotregister");
        } else {
            return ok("account.registered");
        }
    }
}
