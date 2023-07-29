package com.kob.backend.controller.pk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/pk/")
public class IndexController {
    @RequestMapping("index/")
    public Map<String,String> index() {
        Map<String,String> map = new HashMap<>();
        map.put("name","PK");
        map.put("age","18");
        return map;
    }
}
