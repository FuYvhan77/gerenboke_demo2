package com.hxy.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hxy.mapper.UserMapper;
import com.hxy.pojo.User;
import com.hxy.result.ResponseResult;
import com.hxy.service.LoginService;
import com.hxy.utils.JwtUtil;
import com.hxy.utils.UserContextHolder;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangyadong
 * @version 1.0
 * @ClassName LoginServiceImpl
 * @date 2026-04-10 14:06
 */

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;



    @Override
    public ResponseResult login(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, user.getUserName());
        User dbUser = userMapper.selectOne(queryWrapper);

        if (dbUser == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        if (!BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        String userId = dbUser.getId().toString();
        String token = JwtUtil.createJWT(userId);
        redisTemplate.opsForValue().set("adminlogin:" + userId, JSON.toJSONString(dbUser), 1, TimeUnit.DAYS);

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        return ResponseResult.okResult(map);
    }

    @Override
    public void logout() {
        User user = UserContextHolder.getUser();
        redisTemplate.delete("adminlogin:" + user.getId());
    }
}
