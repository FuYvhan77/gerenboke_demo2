package com.hxy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 管理员用户信息VO
 * 封装权限、角色、用户基本信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserInfoVo {
    private List<String> permissions; // 权限标识列表
    private List<String> roles; // 角色标识列表
    private UserInfoVo user; // 用户基本信息
}