package com.hxy.controller;

import com.hxy.pojo.Menu;
import com.hxy.pojo.User;
import com.hxy.result.ResponseResult;
import com.hxy.service.LoginService;
import com.hxy.service.MenuService;
import com.hxy.service.RoleService;
import com.hxy.utils.BeanCopyUtils;
import com.hxy.utils.UserContextHolder;
import com.hxy.vo.AdminUserInfoVo;
import com.hxy.vo.RoutersVo;
import com.hxy.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhangyadong
 * @version 1.0
 * @ClassName LoginController
 * @date 2026-04-10 14:04
 */

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user) {
        return loginService.login(user);
    }


    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;


    @GetMapping("/getInfo")
    public ResponseResult getInfo() {
        //1,获取用户信息
        User user = UserContextHolder.getUser();

        //2,获取权限信息
        List<String> perms = menuService.selectPermsByUserId(user.getId());

        //3,获取用户角色
        List<String> roles = roleService.selectRolesByUserId(user.getId());


        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roles, BeanCopyUtils.copyBean(user, UserInfoVo.class));

        return ResponseResult.okResult(adminUserInfoVo);
    }


    /**
     * 获取用户可访问的菜单路由（树形结构）
     * @return 响应结果
     */
    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        // 1. 获取当前用户ID
        Long userId = UserContextHolder.getUser().getId();

        // 2. 查询用户可访问的菜单（树形结构）
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);

        // 3. 封装返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }


    @PostMapping("/user/logout")
    public ResponseResult logout() {
        loginService.logout();
        return ResponseResult.okResult();
    }
}
