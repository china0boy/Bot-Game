package com.kob.backend.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private static final ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    private static final CopyOnWriteArraySet<User> matchpool = new CopyOnWriteArraySet<>();

    private User user;
    private Session session = null;

    private static UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
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
            matchpool.remove(this.user);
        }
    }

    @SneakyThrows
    private void startMatch() {
        System.out.println("开始匹配");
        matchpool.add(this.user);
        while (matchpool.size() >= 2) {
            Iterator<User> iterator = matchpool.iterator();
            User user1 = iterator.next();
            User user2 = iterator.next();
            matchpool.remove(user1);
            matchpool.remove(user2);

            Game game = new Game(13,14,20);
            game.createMap();

            JSONObject respUser1 = new JSONObject();
            respUser1.put("type", "start_match");
            respUser1.put("opponent_username", user2.getUsername());
            respUser1.put("opponent_photo", user2.getPhoto());
            respUser1.put("gamemap", game.getG());
            users.get(user1.getId()).sendMessage(respUser1.toJSONString());

            JSONObject respUser2 = new JSONObject();
            respUser2.put("type", "start_match");
            respUser2.put("opponent_username", user1.getUsername());
            respUser2.put("opponent_photo", user1.getPhoto());
            respUser2.put("gamemap", game.getG());
            users.get(user2.getId()).sendMessage(respUser2.toJSONString());

        }
    }

    private void cancelMatch() {
        System.out.println("取消匹配");
        matchpool.remove(this.user);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("接收到消息: " + message);
        JSONObject data = JSONObject.parseObject(message);
        String type = data.getString("type");
        if ("match".equals(type)) {
            startMatch();
        } else if ("cancel_match".equals(type)) {
            cancelMatch();
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