package com.hxy.service;

import com.hxy.pojo.User;
import com.hxy.result.ResponseResult;

public interface LoginService {
    ResponseResult login(User user);

    void logout();
}
