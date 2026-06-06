package com.hxy.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagListDto {
    /** 标签名（支持模糊查询） */
    private String name;
    /** 标签备注（支持模糊查询） */
    private String remark;
    /** 当前页 */
    private Integer pageNum;
    /** 每页大小 */
    private Integer pageSize;
}