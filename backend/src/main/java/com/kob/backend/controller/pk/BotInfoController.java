package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/PK/")
public class BotInfoController {
    @RequestMapping("botInfo/")
    public String botInfo() {
        return "PK/botInfo.html";
    }
}
