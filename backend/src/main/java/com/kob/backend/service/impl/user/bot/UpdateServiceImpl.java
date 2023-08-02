package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.kob.backend.utils.UserUtil.getUser;

@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {
        Map<String, String> map = new HashMap<>();
        User user = getUser();
        String bot_id =data.get("bot_id");
        String title = data.get("title"); // 标题
        String description = data.get("description"); // 描述
        String content = data.get("content"); // 内容
        if(bot_id ==null) {
            map.put("message", "bot_id不能为空");
            return map;
        }
        if (title.trim().length() == 0) {
            map.put("message", "bot名称不能为空");
            return map;
        }
        if (title.length() > 100) {
            map.put("message", "bot名称长度不能超过100");
            return map;
        }
        if (description.trim().length() != 0 && description.length() > 300) {
            map.put("message", "bot描述长度不能超过300");
            return map;
        }
        if (content.trim().length() == 0) {
            map.put("message", "代码不能为空");
            return map;
        }
        if (content.length() > 10000) {
            map.put("message", "代码长度不能超过10000");
            return map;
        }
        Bot bot = botMapper.selectById(bot_id);
        if (bot == null) {
            map.put("message", "bot不存在");
            return map;
        }
        if (!Objects.equals(bot.getUserId(), user.getId())) {
            map.put("message", "无权限修改此bot");
            return map;
        }
        Bot new_bot = new Bot(bot.getId(), user.getId(), title, description, content, bot.getRating(), bot.getCreatetime(), new Date());
        botMapper.updateById(new_bot);
        map.put("message", "修改成功");
        return map;
    }
}
