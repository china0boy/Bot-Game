package com.botrunningsystem.utils;
import java.util.ArrayList;
import java.util.List;

public class Bot implements BotInterface {
    static class Cell {
        Integer x, y;

        public Cell(Integer x, Integer y) {
            this.x = x;
            this.y = y;
        }
    }

    public boolean checkTailIncreasing(int step) {//检查尾巴是否增长，前十步都是增长，之后每三步增长一次
        if (step <= 10) return true;
        return step % 3 == 1;
    }

    public List<Cell> getCells(int sx, int sy, String steps) {//根据起始点和步骤字符串，返回蛇的坐标
        steps = steps.substring(1, steps.length() - 1);
        List<Cell> res = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Cell(x, y));
        for (int i = 0; i < steps.length(); i++) {
            int d = steps.charAt(i) - '0';
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if (!checkTailIncreasing(++step)) {
                res.remove(0);
            }
        }
        return res;
    }

    @Override
    public Integer nextMove(String input) {
        String[] strs = input.split("#");
        int[][] g = new int[13][14];//解析地图 1是障碍物 0是空地
        for (int i = 0, k = 0; i < 13; i++) {
            for (int j = 0; j < 14; j++, k++) {
                if (strs[0].charAt(k) == '1') g[i][j] = 1;
            }
        }

        //1是自己蛇的x，2是自己蛇的y，3是自己蛇的方向，4是对方蛇的x，5是对方蛇的y，6是对方蛇的方向
        int aSx = Integer.parseInt(strs[1]), aSy = Integer.parseInt(strs[2]);
        int bSx = Integer.parseInt(strs[4]), bSy = Integer.parseInt(strs[5]);
        List<Cell> aCells = getCells(aSx, aSy, strs[3]);
        List<Cell> bCells = getCells(bSx, bSy, strs[6]);

        for (Cell c : aCells) g[c.x][c.y] = 1;
        for (Cell c : bCells) g[c.x][c.y] = 1;

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        for(int i=0;i<4;i++){
            int x=aCells.get(aCells.size()-1).x+dx[i];
            int y=aCells.get(aCells.size()-1).y+dy[i];
            if(x>=0&&x<13&&y>=0&&y<14&&g[x][y]==0){
                return i;
            }
        }

        return 0;
    }
}
