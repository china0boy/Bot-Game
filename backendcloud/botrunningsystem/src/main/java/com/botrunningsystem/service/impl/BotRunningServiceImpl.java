package com.botrunningsystem.service.impl;

import com.botrunningsystem.service.BotRunningService;
import com.botrunningsystem.service.impl.utils.BotPool;
import org.springframework.stereotype.Service;

@Service
public class BotRunningServiceImpl implements BotRunningService {
    public static final BotPool botPool = new BotPool();

    @Override
    public String addBot(Integer userId, String boyCode, String input) {
//        System.out.println("userId: " + userId+ " boyCode: " + boyCode + " input: " + input);
        botPool.addBot(userId, boyCode, input);
        return "添加bot";
    }
}
