package com.hxy.vo;


import com.hxy.pojo.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 路由VO
 * 封装用户可访问的菜单列表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutersVo {
    private List<Menu> menus; // 菜单列表（树形结构）
}