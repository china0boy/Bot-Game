package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.kob.backend.utils.UserUtil.getUser;

@Service
public class RemoveServiceImpl implements RemoveService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> remove(Map<String, String> data) {
        Map<String, String> map =new HashMap<>();
        User user = getUser();
        int bot_id = Integer.parseInt(data.get("bot_id"));
        Bot bot= botMapper.selectById(bot_id);
        if (bot == null) {
            map.put("status", "error");
            map.put("message", "bot不存在");
            return map;
        }
        if (!Objects.equals(bot.getUserId(), user.getId())) {
            map.put("status", "error");
            map.put("message", "无权限删除此bot");
            return map;
        }
        botMapper.deleteById(bot_id);
        map.put("status", "success");
        map.put("message", "删除成功");
        return map;
    }
}
