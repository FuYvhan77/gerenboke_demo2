package com.hxy.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangyadong
 * @version 1.0
 * @ClassName CategoryVo
 * @date 2023/7/21 16:35
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVo {

    private Long id;

    /**
     * 分类名
     */
    private String name;
}