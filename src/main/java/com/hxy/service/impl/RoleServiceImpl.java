package com.hxy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxy.pojo.Role;
import com.hxy.result.ResponseResult;
import com.hxy.service.RoleService;
import com.hxy.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author zhangyadong
* @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
* @createDate 2026-04-14 11:25:34
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Override
    public List<String> selectRolesByUserId(Long id) {
        if (id==1L){
            return List.of("admin");
        }
        List<Role> roles = baseMapper.selectRolesByUserId(id);
        return roles.stream().map(Role::getRoleKey).toList();
    }
}




