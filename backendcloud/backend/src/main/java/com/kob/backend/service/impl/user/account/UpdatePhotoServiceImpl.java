package com.kob.backend.service.impl.user.account;

import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.UpdatePhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.kob.backend.utils.UserUtil.getUser;

@Service
public class UpdatePhotoServiceImpl implements UpdatePhotoService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> updatePhoto(String photo) {
        Map<String, String> map = new HashMap<>();
        if (photo == null) {
            photo = "";
        }
        if (photo.length() > 1000) {
            map.put("status", "error");
            map.put("message", "头像链接过长");
            return map;
        }
        User user=getUser();
        user.setPhoto(photo);
        userMapper.updateById(user);
        map.put("status", "success");
        map.put("message", "修改成功");
        return map;
    }
}
