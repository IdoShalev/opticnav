package opticnav.web.rest;

import java.sql.Connection;

import javax.servlet.http.HttpServletResponse;

import opticnav.persistence.web.DBUtil;
import opticnav.persistence.web.PublicBroker;
import opticnav.web.components.UserSession;
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
    
    @Autowired
    private UserSession userSession;
    
    @RequestMapping(value="login", method=RequestMethod.POST)
    public Message login(@RequestBody Account account)
            throws Exception {
        boolean valid;
        int accountID;
        
        try (Connection conn = DBUtil.getConnectionFromDataSource(dbDataSource)) {
            PublicBroker db = new PublicBroker(conn);
            accountID = db.verify(account.username, account.password);
        }
        
        valid = accountID != 0;
        
        if (valid) {
            this.userSession.setUser(account.username, accountID);
            return ok("account.loggedin");
        } else {
            this.userSession.resetUser();
            throw new BadRequest("account.couldnotlogin");
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

        try (Connection conn = DBUtil.getConnectionFromDataSource(dbDataSource)) {
            PublicBroker db = new PublicBroker(conn);
            registered = db.registerAccount(account.username, account.password);
        }
        
        if (registered) {
            return ok("account.registered");
        } else {
            throw new BadRequest("account.couldnotregister");
        }
    }
}
