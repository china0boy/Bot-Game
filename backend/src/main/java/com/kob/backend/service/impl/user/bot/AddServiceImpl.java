package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.kob.backend.utils.UserUtil.getUser;

@Service
public class AddServiceImpl implements AddService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {
        Map<String, String> map = new HashMap<>();
        User user = getUser();
        String title = data.get("title"); // 标题
        String description = data.get("description"); // 描述
        String content = data.get("content"); // 内容
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
        Date date = new Date();
        Bot bot = new Bot(null, user.getId(), title, description, content, 1500, date, date);
        botMapper.insert(bot);
        map.put("message", "success");
        return map;
    }
}
