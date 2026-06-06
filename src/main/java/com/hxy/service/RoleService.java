package com.hxy.service;

import com.hxy.pojo.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author zhangyadong
* @description 针对表【sys_role(角色信息表)】的数据库操作Service
* @createDate 2026-04-14 11:25:34
*/
public interface RoleService extends IService<Role> {

    List<String> selectRolesByUserId(Long id);
}
