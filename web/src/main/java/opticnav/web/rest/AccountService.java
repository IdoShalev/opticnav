package opticnav.web.rest;

import javax.servlet.http.HttpServletResponse;

import opticnav.persistence.web.WebDBBroker;
import opticnav.web.rest.pojo.Account;
import opticnav.web.rest.pojo.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/account/**")
public class AccountService extends Controller {
    @Autowired
    private javax.sql.DataSource dbDataSource;
    
    @RequestMapping(value="login", method=RequestMethod.POST)
    public Message login(@RequestBody Account account, HttpServletResponse resp)
            throws Exception {
        boolean valid;
        int accountID;
        
        try (WebDBBroker db = new WebDBBroker(dbDataSource)) {
            accountID = db.verify(account.username, account.password);
        }
        
        valid = accountID != 0;
        
        if (valid) {
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
    public Message register(@RequestBody Account account, HttpServletResponse resp)
            throws Exception {
        boolean registered;

        try (WebDBBroker db = new WebDBBroker(dbDataSource)) {
            registered = db.registerAccount(account.username, account.password);
        }
        
        if (registered) {
            return ok("account.registered");
        } else {
            return badRequest("account.couldnotregister");
        }
    }
}
