package com.kob.backend.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {
    public static final ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();

    private User user;
    private Session session = null;

    public static UserMapper userMapper;
    public static RecordMapper recordMapper;
    private static BotMapper botMapper;
    public static RestTemplate restTemplate;

    public Game game = null;
    private final static String addPlayerUrl = "http://127.0.0.1:8012/player/add/";
    private final static String removePlayerUrl = "http://127.0.0.1:8012/player/remove/";

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }

    @Autowired
    public void setBotMapper(BotMapper botMapper) {
        WebSocketServer.botMapper = botMapper;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        this.session = session;
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);
        if (this.user == null) {
            System.out.println("用户不存在");
            this.session.close();
            return;
        }
        System.out.println("链接token: " + user);
        users.put(userId, this);
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("关闭链接" + this.session.getId());
        if (this.user != null) {
            users.remove(this.user.getId());
            MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
            data.add("userId", user.getId().toString());
            System.out.println(restTemplate.postForObject(removePlayerUrl, data, String.class));
        }
    }

    @SneakyThrows
    public static void starGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        User user1 = userMapper.selectById(aId), user2 = userMapper.selectById(bId);
        Bot bot1 = botMapper.selectById(aBotId), bot2 = botMapper.selectById(bBotId);

        Game game = new Game(
                13,
                14,
                20,
                user1.getId(),
                bot1,
                user2.getId(),
                bot2);
        game.createMap();
        if (users.get(user1.getId()) != null) {
            users.get(user1.getId()).game = game;
        }
        if (users.get(user2.getId()) != null) {
            users.get(user2.getId()).game = game;
        }
        game.start();

        JSONObject respGame = new JSONObject();
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());
        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());
        respGame.put("gamemap", game.getG());

        JSONObject respUser1 = new JSONObject();
        respUser1.put("type", "start_match");
        respUser1.put("opponent_username", user2.getUsername());
        respUser1.put("opponent_photo", user2.getPhoto());
        respUser1.put("game", respGame);
        if (users.get(user1.getId()) != null) {
            users.get(user1.getId()).sendMessage(respUser1.toJSONString());
        }

        JSONObject respUser2 = new JSONObject();
        respUser2.put("type", "start_match");
        respUser2.put("opponent_username", user1.getUsername());
        respUser2.put("opponent_photo", user1.getPhoto());
        respUser2.put("game", respGame);
        if (users.get(user2.getId()) != null) {
            users.get(user2.getId()).sendMessage(respUser2.toJSONString());
        }
    }

    @SneakyThrows
    private void startMatch(Integer botId) {
        System.out.println("开始匹配");
        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        data.add("userId", user.getId().toString());
        data.add("rating", user.getRating().toString());
        data.add("botId", botId.toString());
        System.out.println(restTemplate.postForObject(addPlayerUrl, data, String.class));
    }

    private void cancelMatch() {
        System.out.println("取消匹配");
        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        data.add("userId", user.getId().toString());
        System.out.println(restTemplate.postForObject(removePlayerUrl, data, String.class));
    }

    private void move(int direction) {
        if (game.getPlayerA().getId().equals(user.getId())) {
            if (game.getPlayerA().getBotId().equals(-1))
                game.setNextStepA(direction);
        } else if (game.getPlayerB().getId().equals(user.getId())) {
            if (game.getPlayerB().getBotId().equals(-1))
                game.setNextStepB(direction);
        } else {
            System.out.println("用户不在游戏中");
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
//        System.out.println("接收到消息: " + message);
        JSONObject data = JSONObject.parseObject(message);
        String type = data.getString("type");
        if ("match".equals(type)) {
            startMatch(data.getInteger("bot_id"));
        } else if ("cancel_match".equals(type)) {
            cancelMatch();
        } else if ("move".equals(type)) {
            move(data.getInteger("direction"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) throws Exception {
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}