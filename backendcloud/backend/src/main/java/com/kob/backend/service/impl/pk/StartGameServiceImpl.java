package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl implements StartGameService {

    @Override
    public String startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        WebSocketServer.starGame(aId, aBotId, bId, bBotId);
        return aId + "和" + bId + "匹配成功";
    }
}
