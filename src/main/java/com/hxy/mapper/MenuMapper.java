package com.hxy.mapper;

import com.hxy.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author zhangyadong
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Mapper
* @createDate 2026-04-14 11:25:42
* @Entity com.hxy.pojo.Menu
*/
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> selectPermsByUserId(Long id);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuTreeByUserId(Long userId);
}




