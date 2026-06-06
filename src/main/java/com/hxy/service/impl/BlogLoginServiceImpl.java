package com.hxy.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hxy.enums.AppHttpCodeEnum;
import com.hxy.exception.SystemException;
import com.hxy.mapper.UserMapper;
import com.hxy.pojo.User;
import com.hxy.result.ResponseResult;
import com.hxy.service.BlogLoginService;
import com.hxy.utils.BeanCopyUtils;
import com.hxy.utils.JwtUtil;
import com.hxy.utils.UserContextHolder;
import com.hxy.vo.UserInfoVo;
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
 * @ClassName BlogLoginServiceImpl
 * @date 2026-04-08 10:39
 */

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public ResponseResult logout() {
        //1. 获取用户信息
        User user = UserContextHolder.getUser();
        Long id = user.getId();
        //3. 删除Redis中的用户信息
        stringRedisTemplate.delete("bloglogin:" + id);
        //4. 清空用户信息
        UserContextHolder.clear();
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult login(User user) {
        //1. 接收账号密码
        //2. 根据用户名查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,user.getUserName());
        User dbUser = userMapper.selectOne(queryWrapper);
        if (dbUser == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
        }
        //3. BCrypt比对密码
        if (!BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
        }
        //4. 生成JWT令牌
        String jwt = JwtUtil.createJWT(dbUser.getId().toString());
        //5. 用户信息存入Redis
        stringRedisTemplate.opsForValue().set("bloglogin:" + dbUser.getId(), JSON.toJSONString(dbUser), 1, TimeUnit.DAYS);        //6. 返回token+用户信息

        UserInfoVo vo = BeanCopyUtils.copyBean(dbUser, UserInfoVo.class);
        Map<String, Object> map = new HashMap<>();
        map.put("token", jwt);
        map.put("userInfo", vo);
        return ResponseResult.okResult(map);
    }
}
