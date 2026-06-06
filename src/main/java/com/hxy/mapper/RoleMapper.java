package com.hxy.mapper;

import com.hxy.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author zhangyadong
* @description 针对表【sys_role(角色信息表)】的数据库操作Mapper
* @createDate 2026-04-14 11:25:34
* @Entity com.hxy.pojo.Role
*/
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> selectRolesByUserId(Long id);
}




