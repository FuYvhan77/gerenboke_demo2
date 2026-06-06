package com.hxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxy.pojo.Menu;
import com.hxy.pojo.SystemConstants;
import com.hxy.service.MenuService;
import com.hxy.mapper.MenuMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyadong
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
 * @createDate 2026-04-14 11:25:42
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
        implements MenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
        if (id == 1L) {
            // 管理员
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(Menu::getPerms);
            queryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            queryWrapper.isNotNull(Menu::getPerms);
            List<Menu> list = list(queryWrapper);
            return list.stream().map(Menu::getPerms).toList();
        }

        List<Menu> list = getBaseMapper().selectPermsByUserId(id);
        return list.stream().map(Menu::getPerms).toList();


    }

    /**
     * 查询用户可访问的菜单（树形结构）
     * @param userId 用户ID
     * @return 树形菜单列表
     */
    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus;

        // 超级管理员：查询所有菜单
        if (userId == 1L) {
            menus = menuMapper.selectAllRouterMenu();
        } else {
            // 普通用户：查询关联菜单
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        // 构建树形结构
        return builderMenuTree(menus, 0L);
    }

    /**
     * 递归构建菜单树形结构
     * @param menus 菜单列表
     * @param parentId 父菜单ID（初始为0，根菜单）
     * @return 树形菜单
     */
    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> treeMenus = new ArrayList<>();
        for (Menu menu : menus) {
            // 匹配当前父菜单的子菜单
            if (menu.getParentId().equals(parentId)) {
                // 递归查询子菜单的子菜单
                menu.setChildren(builderMenuTree(menus, menu.getId()));
                treeMenus.add(menu);
            }
        }
        return treeMenus;
    }

}




