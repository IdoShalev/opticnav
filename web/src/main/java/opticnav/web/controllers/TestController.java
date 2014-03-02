package opticnav.web.controllers;

import opticnav.persistence.web.WebDBBroker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @Autowired
    private javax.sql.DataSource dbDataSource;
    
    @RequestMapping(value="/TestController")
    @ResponseBody
    public String controller() throws Exception {
        try (WebDBBroker db = new WebDBBroker(dbDataSource)) {
            boolean r = db.registerAccount("derp", "herp");

            return r ? "Registered" : "Not registered";
        }
    }
}
