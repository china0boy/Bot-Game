package com.kob.backend.service.impl.user.account;

import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.net.Authenticator;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password) throws Exception {
        Map<String, String> map = new HashMap<>();
        // 将前端传来的username和password进行封装成 authenticationToken
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        // 将封装好的authenticationToken进行验证，判断是否合法，非法会自动报异常
        try {
            Authentication authenticate = authenticationManager.authenticate(token);
            // 若合法，则将其取出并赋予用户UserDetailsImpl类中的各种属性，形成loginUser
            UserDetailsImpl longinUser = (UserDetailsImpl) authenticate.getPrincipal();
            // 单独取loginUser中的用户信息，用于生成JWT-token
            User user = longinUser.getUser();
            String jwt = JwtUtil.createJWT(user.getId().toString());

            map.put("message", "success");
            map.put("token", jwt);
            return map;
        } catch (Exception e) {
            map.put("message", "用户名或密码错误");
            return map;
        }
    }
}
