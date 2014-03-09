package opticnav.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tests/**")
public class TestPagesController {
    @RequestMapping("/resources")
    public String index() {
        return "tests/resources";
    }
}
