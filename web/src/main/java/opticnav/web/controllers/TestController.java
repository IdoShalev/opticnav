package opticnav.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    @RequestMapping(value="/Test/Controller")
    public String controller(Model model) {
        model.addAttribute("foo", "<b>bar</b>");
        return "hello";
        /*
        ModelAndView view = new ModelAndView();
        view.setViewName("test");
        
        return view;
        */
    }
}
