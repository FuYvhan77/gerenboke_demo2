package com.hxy.vo;

import com.hxy.pojo.Tag;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVo {
    /** 总记录数 */
    private Long total;
    /** 当前页数据列表 */
    private List<Tag> rows;
}