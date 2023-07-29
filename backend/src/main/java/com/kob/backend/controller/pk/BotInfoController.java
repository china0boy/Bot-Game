package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pk/")
public class BotInfoController {
    @RequestMapping("botInfo/")
    public Map<String,String> botInfo() {
        Map<String,String> map = new HashMap<>();
        map.put("name","PK");
        map.put("age","18");
        return map;
    }
}
