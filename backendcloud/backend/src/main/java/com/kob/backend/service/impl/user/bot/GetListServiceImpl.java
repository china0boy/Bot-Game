package com.kob.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.bot.AddService;
import com.kob.backend.service.user.bot.GetListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kob.backend.utils.UserUtil.getUser;

@Service
public class GetListServiceImpl implements GetListService {
    @Autowired
    private BotMapper botMapper;
    @Autowired
    private AddService addService;
    @Override
    public List<Bot> getList() {
        User user = getUser();
        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        List<Bot> bots = botMapper.selectList(queryWrapper);
        if (bots.size() == 0) {
            userAddDefaulBot();
            bots = botMapper.selectList(queryWrapper);
        }
        return bots;
    }



    private void userAddDefaulBot(){
        Map<String, String> data = new HashMap<>();
        data.put("title", "默认bot");
        data.put("description", "这是一个教程bot");
        data.put("content", "package com.botrunningsystem.utils;\n" +
                "import java.io.File;\n" +
                "import java.io.FileNotFoundException;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "import java.util.Scanner;\n" +
                "import java.util.function.Supplier;\n" +
                "\n" +
                "public class Bot implements Supplier<Integer> {\n" +
                "\n" +
                "    static class Cell {\n" +
                "        Integer x, y;\n" +
                "\n" +
                "        public Cell(Integer x, Integer y) {\n" +
                "            this.x = x;\n" +
                "            this.y = y;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public boolean checkTailIncreasing(int step) {//检查尾巴是否增长，前十步都是增长，之后每三步增长一次\n" +
                "        if (step <= 10) return true;\n" +
                "        return step % 3 == 1;\n" +
                "    }\n" +
                "\n" +
                "    public List<Cell> getCells(int sx, int sy, String steps) {//根据起始点和步骤字符串，返回蛇的坐标\n" +
                "        steps = steps.substring(1, steps.length() - 1);\n" +
                "        List<Cell> res = new ArrayList<>();\n" +
                "        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};\n" +
                "        int x = sx, y = sy;\n" +
                "        int step = 0;\n" +
                "        res.add(new Cell(x, y));\n" +
                "        for (int i = 0; i < steps.length(); i++) {\n" +
                "            int d = steps.charAt(i) - '0';\n" +
                "            x += dx[d];\n" +
                "            y += dy[d];\n" +
                "            res.add(new Cell(x, y));\n" +
                "            if (!checkTailIncreasing(++step)) {\n" +
                "                res.remove(0);\n" +
                "            }\n" +
                "        }\n" +
                "        return res;\n" +
                "    }\n" +
                "\n" +
                "    public Integer nextMove(String input) {\n" +
                "        String[] strs = input.split(\"#\");\n" +
                "        int[][] g = new int[13][14];//解析地图 1是障碍物 0是空地\n" +
                "        for (int i = 0, k = 0; i < 13; i++) {\n" +
                "            for (int j = 0; j < 14; j++, k++) {\n" +
                "                if (strs[0].charAt(k) == '1') g[i][j] = 1;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        //1是自己蛇的x，2是自己蛇的y，3是自己蛇的方向，4是对方蛇的x，5是对方蛇的y，6是对方蛇的方向\n" +
                "        int aSx = Integer.parseInt(strs[1]), aSy = Integer.parseInt(strs[2]);\n" +
                "        int bSx = Integer.parseInt(strs[4]), bSy = Integer.parseInt(strs[5]);\n" +
                "        List<Cell> aCells = getCells(aSx, aSy, strs[3]);\n" +
                "        List<Cell> bCells = getCells(bSx, bSy, strs[6]);\n" +
                "\n" +
                "        for (Cell c : aCells) g[c.x][c.y] = 1;\n" +
                "        for (Cell c : bCells) g[c.x][c.y] = 1;\n" +
                "\n" +
                "        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};\n" +
                "        for(int i=0;i<4;i++){\n" +
                "            int x=aCells.get(aCells.size()-1).x+dx[i];\n" +
                "            int y=aCells.get(aCells.size()-1).y+dy[i];\n" +
                "            if(x>=0&&x<13&&y>=0&&y<14&&g[x][y]==0){\n" +
                "                return i;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        return 0;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public Integer get() {\n" +
                "        File file = new File(\"input.txt\");\n" +
                "        try {\n" +
                "            Scanner sc=new Scanner(file);\n" +
                "            return nextMove(sc.next());\n" +
                "        } catch (FileNotFoundException e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }\n" +
                "    }\n" +
                "}\n");
        addService.add(data);
    }
}
