package com.kob.backend.service.impl.user.account;

import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.InfoService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.kob.backend.utils.UserUtil.getUser;

@Service
public class InfoServiceImpl implements InfoService {
    @Override
    public Map<String, String> getInfo() throws Exception {
        Map<String, String> map = new HashMap<>();
        try {
            User user = getUser();
            map.put("message", "success");
            map.put("id", user.getId().toString());
            map.put("username", user.getUsername());
            map.put("photo", user.getPhoto());
            return map;
        } catch (Exception e) {
            map.put("message", "找不到用户信息");
            return map;
        }
    }
}
