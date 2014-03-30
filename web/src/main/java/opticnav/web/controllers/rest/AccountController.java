package opticnav.web.controllers.rest;

import javax.servlet.http.HttpServletResponse;

import opticnav.persistence.web.WebAccountDAO;
import opticnav.persistence.web.WebAccountPublicDAO;
import opticnav.web.components.UserSession;
import opticnav.web.components.UserSession.User;
import opticnav.web.controllers.rest.pojo.Account;
import opticnav.web.controllers.rest.pojo.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account/**")
public class AccountController extends Controller {
    public static final class QueryPOJO {
        public String username;
        public int id;
        
        public QueryPOJO(String username, int id) {
            this.username = username;
            this.id = id;
        }
    }
    
    public static final class AccountPOJO {
        public String username;
        public int id;
        
        public AccountPOJO(String username, int id) {
            this.username = username;
            this.id = id;
        }
    }
    
    @Autowired
    private javax.sql.DataSource dbDataSource;
    
    @Autowired
    private UserSession userSession;
    
    @RequestMapping(value="login", method=RequestMethod.POST)
    public Message login(@RequestBody Account account)
            throws Exception {
        boolean valid;
        int accountID;
        
        try (WebAccountPublicDAO dao = new WebAccountPublicDAO(dbDataSource)) {
            accountID = dao.verify(account.username, account.password);
        }
        
        valid = accountID != WebAccountPublicDAO.ACCOUNT_ID_NONE;
        
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

        try (WebAccountPublicDAO dao = new WebAccountPublicDAO(dbDataSource)) {
            registered = dao.registerAccount(account.username, account.password);
        }
        
        if (registered) {
            return ok("account.registered");
        } else {
            throw new BadRequest("account.couldnotregister");
        }
    }
    
    @RequestMapping(value="/query/{username}", method=RequestMethod.GET)
    public QueryPOJO query(@PathVariable String username) throws Exception {
        int id;
        String name;
        try (WebAccountPublicDAO dao = new WebAccountPublicDAO(dbDataSource)) {
            id = dao.findName(username);
        }
        try (WebAccountDAO dao = new WebAccountDAO(dbDataSource, id)) {
            name = dao.getUsername();
        }
        if (id == WebAccountPublicDAO.ACCOUNT_ID_NONE) {
            throw new NotFound("account.query.notfound", username);
        }
        
        return new QueryPOJO(name, id);
    }
    
    @RequestMapping(value="/current", method=RequestMethod.GET)
    public AccountPOJO getCurrentAccount() {
        final User user = userSession.getUser();
        return new AccountPOJO(user.getUsername(), user.getId());
    }
}
