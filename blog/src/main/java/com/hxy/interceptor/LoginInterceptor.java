package com.hxy.interceptor;

import com.alibaba.fastjson2.JSON;
import com.hxy.enums.AppHttpCodeEnum;
import com.hxy.pojo.User;
import com.hxy.result.ResponseResult;
import com.hxy.utils.JwtUtil;
import com.hxy.utils.UserContextHolder;
import com.hxy.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import java.io.PrintWriter;

/**
 * @author zhangyadong
 * @version 1.0
 * @ClassName LoginInterceptor
 * @date 2026-04-08 14:28
 */

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1, 获取请求token
        String token = request.getHeader("Token");
        //2, 判断token是否为空
        if (!StringUtils.hasText(token)) {//null   ""  "   "
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return false;
        }

        //3. 解析token
        String userId;
        try {
            userId = JwtUtil.parseJWT(token).getSubject();
        } catch (Exception e) {
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return false;
        }
        //4. 获取用户id
        String userJson = stringRedisTemplate.opsForValue().get("bloglogin:" + userId);
        if (!StringUtils.hasText(userJson)) {
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return false;
        }

        // 验证通过，存入ThreadLocal
        User user = JSON.parseObject(userJson, User.class);
        UserContextHolder.setUser(user);


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
