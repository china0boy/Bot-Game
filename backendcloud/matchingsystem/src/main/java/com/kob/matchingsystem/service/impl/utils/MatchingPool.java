package com.kob.matchingsystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MatchingPool extends Thread {
    private static List<Player> players = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();
    private static RestTemplate restTemplate;
    private final static String sendResultUrl = "http://127.0.0.1:8011/pk/start/game/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }

    public void addPlayer(Integer userId, Integer rating , Integer botId) {
        lock.lock();
        try {
            for (Player player : players) {
                if (player.getUserId().equals(userId)) {
                    return;
                }
            }
            players.add(new Player(userId, rating, botId, 0));
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId) {
        lock.lock();
        try {
            List<Player> newPlayers = new ArrayList<>();
            for (Player player : players) {
                if (!player.getUserId().equals(userId)) {
                    newPlayers.add(player);
                }
            }
            players = newPlayers;
        } finally {
            lock.unlock();
        }
    }

    private void increaseWaitingTime() {
        for (Player player : players) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    private boolean checkMatched(Player player1, Player player2) {
        int ratingDelta = Math.abs(player1.getRating() - player2.getRating());
        int waitingTime = Math.max(player1.getWaitingTime(), player2.getWaitingTime());
        return ratingDelta <= waitingTime * 10;
    }

    private void sendResult(Player player1, Player player2) {
        MultiValueMap<String,String> data= new LinkedMultiValueMap<>();
        data.add("aId", player1.getUserId().toString());
        data.add("aBotId", player1.getBotId().toString());
        data.add("bId", player2.getUserId().toString());
        data.add("bBotId", player2.getBotId().toString());
        System.out.println(restTemplate.postForObject(sendResultUrl, data, String.class));
    }

    private void matchPlayers() {
        boolean[] used = new boolean[players.size()];
        for (int i = 0; i < players.size(); i++) {
            if (used[i]) {
                continue;
            }
            Player player1 = players.get(i);
            for (int j = i + 1; j < players.size(); j++) {
                if (used[j]) {
                    continue;
                }
                Player player2 = players.get(j);
                if (checkMatched(player1, player2)) {
                    sendResult(player1, player2);
                    used[i] = used[j] = true;
                    break;
                }
            }
        }
        List<Player> newPlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (!used[i]) {
                newPlayers.add(players.get(i));
            }
        }
        players = newPlayers;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    increaseWaitingTime();
                    matchPlayers();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
