package com.kob.backend.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Record;
import lombok.SneakyThrows;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {
    private final Integer rows;
    private final Integer cols;
    private final Integer innerWallsCount;
    private final int[][] g;
    private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    private final Player playerA, playerB;
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private ReentrantLock lock = new ReentrantLock();
    private String status = "playing";   //playing finished
    private String loser = "";  // all：平局   A：A输   B：B输

    public Game(Integer rows, Integer cols, Integer innerWallsCount, Integer playerAId, Integer playerBId) {
        this.rows = rows;
        this.cols = cols;
        this.innerWallsCount = innerWallsCount;
        this.g = new int[rows][cols];
        this.playerA = new Player(playerAId, rows - 2, 1, new ArrayList<>());
        this.playerB = new Player(playerBId, 1, cols - 2, new ArrayList<>());
    }

    public int[][] getG() {
        return g;
    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }

    private boolean draw() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                g[r][c] = 0;
            }
        }

        for (int r = 0; r < rows; r++) {
            g[r][0] = g[r][cols - 1] = 1;
        }

        for (int c = 0; c < cols; c++) {
            g[0][c] = g[rows - 1][c] = 1;
        }

        Random random = new Random();
        for (int i = 0; i < innerWallsCount / 2; i++) {
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(rows);
                int c = random.nextInt(cols);
                if (g[r][c] == 1 || g[rows - 1 - r][cols - 1 - c] == 1) continue;
                if ((r == rows - 2 && c == 1) || (r == 1 && c == cols - 2)) continue;
                g[r][c] = g[rows - 1 - r][cols - 1 - c] = 1;
                break;
            }
        }
//        g[rows - 2][1] = g[1][cols - 2] = 0;


        return checkConnectivity(rows - 2, 1, 1, cols - 2);
    }

    public void createMap() {
        for (int i = 0; i < 1000; i++) {
            if (draw()) break;
        }
    }

    private boolean checkConnectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = 1;


        for (int i = 0; i < 4; i++) {
            int x = sx + dx[i];
            int y = sy + dy[i];
            if (x >= 0 && x < rows && y >= 0 && y < cols && g[x][y] == 0 && checkConnectivity(x, y, tx, ty)) {
                g[sx][sy] = 0;
                return true;
            }
        }
        g[sx][sy] = 0;
        return false;
    }

    private boolean nextStep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 25; i++) {
            try {
                Thread.sleep(200);
                lock.lock();
                try {
                    if (nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean checkValid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        if (g[cell.x][cell.y] == 1) return false;
        for (int i = 0; i < n - 1; i++) {
            if (cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y) return false;
        }
        for (int i = 0; i < n - 1; i++) {
            if (cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y) return false;
        }
        return true;
    }

    private void judge() {
        List<Cell> snakeA = playerA.getCells();
        List<Cell> snakeB = playerB.getCells();

        boolean validA = checkValid(snakeA, snakeB);
        boolean validB = checkValid(snakeB, snakeA);
        if (!validA || !validB) {
            status = "finished";
            if (!validA && !validB) {
                loser = "all";
            } else if (!validA) {
                loser = "A";
            } else {
                loser = "B";
            }
        }
    }

    @SneakyThrows
    private void sendAllMessage(String message) {
        if (WebSocketServer.users.get(playerA.getId()) != null) {
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        }
        if (WebSocketServer.users.get(playerB.getId()) != null) {
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
        }
    }

    private void sendMove() {
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("type", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            nextStepA = nextStepB = null;
            sendAllMessage(resp.toJSONString());
        } finally {
            lock.unlock();
        }
    }

    private void sendResult() {
        JSONObject resp = new JSONObject();
        resp.put("type", "result");
        resp.put("loser", loser);
        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }

    private String getMapString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }

    private void saveToDatabase() {
        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );

        WebSocketServer.recordMapper.insert(record);
    }

    @Override
    public void run() {
        for (int i = 0; i < rows * cols / 2; i++) {
            if (nextStep()) {
                judge();
                sendMove();
                if (status.equals("playing")) {

                } else {
                    sendResult();
                    break;
                }
            } else {
                status = "finished";
                lock.lock();
                try {
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else {
                        loser = "B";
                    }

                } finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
