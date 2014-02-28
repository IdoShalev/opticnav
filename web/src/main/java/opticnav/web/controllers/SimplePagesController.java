package opticnav.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SimplePagesController {
    @RequestMapping("/")
    public String index() {
        return "index";
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
}
