package opticnav.web.controllers;

import opticnav.web.components.UserSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SimplePagesController {
    @Autowired
    private UserSession userSession;
    
    @RequestMapping("/")
    public String index() {
        if (this.userSession.getUser() == null) {
            return "index";
        } else {
            return "hub";
        }
    }
    
    @RequestMapping("/logout")
    public String logout() {
        this.userSession.resetUser();
        return "redirect:/";
    }
    
    @RequestMapping("/register")
    public String registerAccount() {
        return "registerAccount";
    }
    
    @RequestMapping("/about")
    public String about() {
        return "about";
    }
    
    @RequestMapping("/contact")
    public String contact() {
        return "contact";
    }
    
    @RequestMapping("/download")
    public String download() {
        return "download";
    }
    
    @RequestMapping("/help")
    public String help() {
        return "help";
    }
    @RequestMapping("/map")
    public String map() {
        return "map";
    }
    @RequestMapping("/instance")
    public String instance() {
        return "instance";
    }
    @RequestMapping("/registerDevice")
    public String registerARD() {
        return "registerARD";
    }
}
