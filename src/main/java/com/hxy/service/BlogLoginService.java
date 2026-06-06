package com.hxy.service;

import com.hxy.pojo.User;
import com.hxy.result.ResponseResult;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
