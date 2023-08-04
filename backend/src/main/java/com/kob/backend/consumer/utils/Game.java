package com.kob.backend.consumer.utils;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    final private Integer rows ;
    final private Integer cols ;
    final private Integer innerWallsCount ;
    final private int[][] g;

    public Game(Integer rows, Integer cols, Integer innerWallsCount) {
        this.rows = rows;
        this.cols = cols;
        this.innerWallsCount = innerWallsCount;
        this.g = new int[rows][cols];
    }

    public int[][] getG() {
        return g;
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

        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};
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
//
//    private boolean creteWalls() {
//        boolean[][] g = new boolean[rows][cols];
//        for (int r = 0; r < rows; r++) {
//            for (int c = 0; c < cols; c++) {
//                g[r][c] = false;
//            }
//        }
//
//        for (int r = 0; r < rows; r++) {
//            g[r][0] = g[r][cols - 1] = true;
//        }
//
//        for (int c = 0; c < cols; c++) {
//            g[0][c] = g[rows - 1][c] = true;
//        }
//
//        Random random = new Random();
//        for (int i = 0; i < innerWallsCount / 2; i++) {
//            for (int j = 0; j < 10000; j++) {
//                int r = random.nextInt(rows);
//                int c = random.nextInt(cols);
//                if (g[r][c] || g[rows - 1 - r][cols - 1 - c]) continue;
//                if ((r == rows - 2 && c == 1) || (r == 1 && c == cols - 2)) continue;
//                g[r][c] = g[rows - 1 - r][cols - 1 - c] = true;
//                break;
//            }
//        }
//
//        g[rows - 2][1] = g[1][cols - 2] = false;
//
//        boolean[][] copyG = new boolean[rows][cols];
//        for (int r = 0; r < rows; r++) {
//            System.arraycopy(g[r], 0, copyG[r], 0, cols);
//        }
//
//        if (!checkConnectivity(copyG, rows - 2, 1, 1, cols - 2)) {
//            return false;
//        }
//
//        for (int r = 0; r < rows; r++) {
//            for (int c = 0; c < cols; c++) {
//                if (g[r][c]) {
//                    walls.add(new Wall(r, c, this));
//                }
//            }
//        }
//        return true;
//    }
//
//    private void addListeningEvents() {
//        ctx.canvas().requestFocus();
//        Snake snake0 = snakes.get(0);
//        Snake snake1 = snakes.get(1);
//
//        ctx.canvas().addKeyListener(new KeyListener() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//            }
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//                int key = e.getKeyCode();
//                switch (key) {
//                    case KeyEvent.VK_W:
//                        snake0.setDirection(0);
//                        break;
//                    case KeyEvent.VK_D:
//                        snake0.setDirection(1);
//                        break;
//                    case KeyEvent.VK_S:
//                        snake0.setDirection(2);
//                        break;
//                    case KeyEvent.VK_A:
//                        snake0.setDirection(3);
//                        break;
//                    case KeyEvent.VK_UP:
//                        snake1.setDirection(0);
//                        break;
//                    case KeyEvent.VK_RIGHT:
//                        snake1.setDirection(1);
//                        break;
//                    case KeyEvent.VK_DOWN:
//                        snake1.setDirection(2);
//                        break;
//                    case KeyEvent.VK_LEFT:
//                        snake1.setDirection(3);
//                        break;
//                }
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//            }
//        });
//    }
//
//    public void start() {
//        for (int i = 0; i < 1000; i++) {
//            if (creteWalls()) {
//                break;
//            }
//        }
//    }
//
//    private void updateSize() {
//        L = Math.min(parent.clientWidth() / cols, parent.clientHeight() / rows);
//        ctx.canvas().width(L * cols);
//        ctx.canvas().height(L * rows);
//    }
//
//    private boolean checkReady() {
//        for (Snake snake : snakes) {
//            if (!"idle".equals(snake.getStatus()) || snake.getDirection() == -1) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public void nextStep() {
//        for (Snake snake : snakes) {
//            snake.nextStep();
//        }
//    }
//
//    private boolean checkValid(Cell cell) {
//        for (Wall wall : walls) {
//            if (wall.getRow() == cell.r && wall.getCol() == cell.c) {
//                return false;
//            }
//        }
//        for (Snake snake : snakes) {
//            int k = snake.getCells().size();
//            if (!snake.checkTailIncreasing()) {
//                k--;
//            }
//            for (int i = 0; i < k; i++) {
//                if (snake.getCells().get(i).r == cell.r && snake.getCells().get(i).c == cell.c) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    public void update() {
//        updateSize();
//        if (checkReady()) {
//            nextStep();
//        }
//        render();
//    }
//
//    private void render() {
//        Color colorEven = new Color(170, 215, 82);
//        Color colorOdd = new Color(162, 208, 72);
//
//        for (int r = 0; r < rows; r++) {
//            for (int c = 0; c < cols; c++) {
//                if ((r + c) % 2 == 0) {
//                    ctx.fillStyle(colorEven);
//                } else {
//                    ctx.fillStyle(colorOdd);
//                }
//                ctx.fillRect(c * L, r * L, L, L);
//            }
//        }
//    }
}
