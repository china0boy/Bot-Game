package com.botrunningsystem.service.impl;

import org.springframework.stereotype.Service;

@Service
public class BotRunningService implements com.botrunningsystem.service.BotRunningService {
    @Override
    public String addBot(Integer userId, String boyCode, String input) {
        System.out.println("userId: " + userId+ " boyCode: " + boyCode + " input: " + input);
        return "添加bot";
    }
}
