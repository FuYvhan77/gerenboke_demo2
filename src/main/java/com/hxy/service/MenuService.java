package com.hxy.service;

import com.hxy.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author zhangyadong
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
* @createDate 2026-04-14 11:25:42
*/
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);
}
