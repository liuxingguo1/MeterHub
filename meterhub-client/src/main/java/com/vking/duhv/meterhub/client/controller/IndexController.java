package com.vking.duhv.meterhub.client.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class IndexController {

    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping("/serviceStatus")
    public String serviceStatus(HttpServletRequest request, Model model) {
        model.addAttribute("name", "test");
        return "serviceStatus";
    }

}
