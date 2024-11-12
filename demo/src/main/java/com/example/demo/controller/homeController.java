package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;

@Controller
public class homeController {
    @RequestMapping(value="/",method = RequestMethod.GET)
    public String index(Model model){
        model.addAttribute("name" , "Apiram");
        return "index.html";
    }
    //RequestMapping(value="/{abs}")
    //public String call(Model model,@PathVariable abs)
}
