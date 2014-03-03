package opticnav.web.controllers;

import opticnav.persistence.web.WebDBBroker;
import opticnav.web.components.UserSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @Autowired
    private javax.sql.DataSource dbDataSource;
    @Autowired
    private UserSession userSession;
    
    @RequestMapping(value="/test/Controller")
    @ResponseBody
    public String controller() throws Exception {
        try (WebDBBroker db = new WebDBBroker(dbDataSource)) {
            boolean r = db.registerAccount("derp", "herp");

            return r ? "Registered" : "Not registered";
        }
    }
    
    @RequestMapping(value="/test/Account")
    @ResponseBody
    public String account() throws Exception {
        UserSession.User user = this.userSession.getUser();
        
        return user != null ? user.getUsername() : "No user";
    }
    
    @RequestMapping(value="/test/AccountSet")
    @ResponseBody
    public boolean accountSet(@RequestParam String username) throws Exception {
        this.userSession.setUser(username, 444);
        return true;
    }
    
    @RequestMapping(value="/auth/")
    @ResponseBody
    public String accountSet() throws Exception {
        return "You need to be logged in to see this!";
    }
}
