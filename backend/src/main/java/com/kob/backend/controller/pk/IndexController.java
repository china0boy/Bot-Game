package com.kob.backend.controller.pk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/PK/")
public class IndexController {
    @RequestMapping("index/")
    public String index() {
        return "PK/index.html";
    }
}
