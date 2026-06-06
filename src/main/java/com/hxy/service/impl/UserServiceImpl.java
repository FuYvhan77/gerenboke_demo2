package com.hxy.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxy.enums.AppHttpCodeEnum;
import com.hxy.pojo.User;
import com.hxy.result.ResponseResult;
import com.hxy.service.UserService;
import com.hxy.mapper.UserMapper;
import com.hxy.utils.BeanCopyUtils;
import com.hxy.utils.UserContextHolder;
import com.hxy.vo.UserInfoVo;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangyadong
 * @description 针对表【sys_user(用户表)】的数据库操作Service实现
 * @createDate 2026-04-08 10:35:14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // ... existing code ...

    /**
     * 获取当前登录用户的个人信息
     * <p>
     * 该方法从用户上下文中获取当前登录的用户信息，并将其转换为UserInfoVo对象返回。
     * </p>
     *
     * @return ResponseResult 封装了响应结果的统一返回对象，包含用户信息（UserInfoVo）
     */
    @Override
    public ResponseResult userInfo() {
        User user = UserContextHolder.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        return ResponseResult.okResult(userInfoVo);
    }

    // ... existing code ...

    /**
     * 更新用户个人信息
     * <p>
     * 该方法根据用户ID更新用户的昵称、头像和性别信息。
     * </p>
     *
     * @param user 用户对象，包含需要更新的用户ID、昵称、头像和性别信息
     * @return ResponseResult 封装了响应结果的统一返回对象，更新成功返回成功状态
     */
    @Override
    public ResponseResult updateUserInfo(User user) {
        // 构建更新条件，根据用户ID更新昵称、头像和性别字段
        LambdaUpdateWrapper<User> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.eq(User::getId, user.getId());
        queryWrapper.set(User::getNickName, user.getNickName());
        queryWrapper.set(User::getAvatar, user.getAvatar());
        queryWrapper.set(User::getSex, user.getSex());
        update(queryWrapper);

        //修改redis数据
        String s = stringRedisTemplate.opsForValue().get("bloglogin:" + user.getId());
        User user1 = JSON.parseObject(s, User.class);
        user1.setAvatar(user.getAvatar());
        user1.setSex(user.getSex());
        user1.setNickName(user.getNickName());
        stringRedisTemplate.delete("bloglogin:" + user.getId());
        stringRedisTemplate.opsForValue().set("bloglogin:" + user.getId(), JSON.toJSONString(user1), 1, TimeUnit.DAYS);

        return ResponseResult.okResult();
    }

    // ... existing code ...

    /**
     * 用户注册功能
     * <p>
     * 该方法处理新用户注册请求，包括参数校验、唯一性检查和密码加密存储。
     * 校验流程：
     * 1. 验证用户名、密码、邮箱、昵称是否为空
     * 2. 检查邮箱是否已被注册
     * 3. 检查用户名是否已被使用
     * 4. 对密码进行BCrypt加密后保存用户信息
     * </p>
     *
     * @param user 用户对象，包含注册所需的用户名、密码、邮箱、昵称等信息
     * @return ResponseResult 封装了响应结果的统一返回对象，成功返回OK状态，失败返回对应的错误码
     */
    @Override
    public ResponseResult register(User user) {
        //非空
        if (user.getUserName() == null || user.getUserName().length() == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (user.getPassword() == null || user.getPassword().length() == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (user.getEmail() == null || user.getEmail().length() == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.EMAIL_EXIST);
        }
        if (user.getNickName() == null || user.getNickName().length() == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //规则  密码  邮箱  正则表达式
        if (!isValidPassword(user.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        if (!isValidEmail(user.getEmail())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }


        //重复
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, user.getEmail());
        long count = count(queryWrapper);
        if (count > 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.EMAIL_EXIST);
        }

        LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(User::getUserName, user.getUserName());
        long count1 = count(queryWrapper1);
        if (count1 > 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
        }

        String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashpw);
        save(user);


        return ResponseResult.okResult();
    }

    /**
     * 验证密码格式
     * <p>
     * 密码必须是6-16位，且必须同时包含数字和字母
     * </p>
     *
     * @param password 待验证的密码
     * @return boolean 密码格式合法返回true，否则返回false
     */
    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 6 || password.length() > 16) {
            return false;
        }
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9a-zA-Z]{6,16}$";
        return password.matches(regex);
    }

    /**
     * 验证邮箱格式
     * <p>
     * 使用标准邮箱格式正则表达式进行验证
     * </p>
     *
     * @param email 待验证的邮箱地址
     * @return boolean 邮箱格式合法返回true，否则返回false
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.length() == 0) {
            return false;
        }
        String regex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        return email.matches(regex);
    }

// ... existing code ...


}




