package com.hxy.service;

import com.hxy.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hxy.result.ResponseResult;

/**
* @author zhangyadong
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2026-04-08 10:35:14
*/
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);
}
