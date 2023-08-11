package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.RegisterService;
import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) throws Exception {
        Map<String, String> map = new HashMap<>();
        try {
            username = username.trim();
            if (username.length() == 0) {
                map.put("status", "error");
                map.put("message", "用户名不能为空");
                return map;
            }
            if (username.length() > 100) {
                map.put("status", "error");
                map.put("message", "用户名太长");
                return map;
            }
            if (password == null || password.length() == 0) {
                map.put("status", "error");
                map.put("message", "密码不能为空");
                return map;
            }
            if (confirmedPassword == null || confirmedPassword.length() == 0) {
                map.put("status", "error");
                map.put("message", "确认密码不能为空");
                return map;
            }
            if (password.length() > 100 || confirmedPassword.length() > 100) {
                map.put("status", "error");
                map.put("message", "密码太长");
                return map;
            }
            if (!password.equals(confirmedPassword)) {
                map.put("status", "error");
                map.put("message", "两次输入的密码不一致");
                return map;
            }
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            User users = userMapper.selectOne(queryWrapper);
            if (users != null) {
                map.put("status", "error");
                map.put("message", "用户名已存在");
                return map;
            }
            String encodedPassword = passwordEncoder.encode(password);
            String photo = "https://cdn.acwing.com/media/user/profile/photo/322947_lg_48981c6a12.jpg";
            User user = new User(null, username, encodedPassword, photo,1500);
            userMapper.insert(user);
            map.put("status", "success");
            map.put("message", "注册成功");
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "error");
            map.put("message", "注册失败");
            return map;
        }
    }
}
