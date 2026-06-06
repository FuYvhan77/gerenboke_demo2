package com.hxy.controller;


import com.hxy.pojo.User;
import com.hxy.result.ResponseResult;
import com.hxy.service.BlogLoginService;
import com.hxy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    @PostMapping("login")
    public ResponseResult login(@RequestBody User user){
        return blogLoginService.login(user);
    }


    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }

}
