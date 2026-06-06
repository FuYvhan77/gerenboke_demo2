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
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return false;
        }

        String userId;
        try {
            userId = JwtUtil.parseJWT(token).getSubject();
        } catch (Exception e) {
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return false;
        }

        String userJson = redisTemplate.opsForValue().get("adminlogin:" + userId);
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
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求结束清空ThreadLocal
        UserContextHolder.clear();
    }
}