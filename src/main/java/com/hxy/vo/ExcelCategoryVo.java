package com.hxy.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类Excel导出VO
 * 映射Excel列与分类数据字段，指定Excel表头名称
 * @Author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelCategoryVo {
    /** 分类名 - Excel表头显示「分类名」 */
    @ExcelProperty("分类名")
    private String name;

    /** 分类描述 - Excel表头显示「描述」 */
    @ExcelProperty("描述")
    private String description;

    /** 分类状态 - Excel表头显示「状态0:正常,1禁用」 */
    @ExcelProperty("状态0:正常,1禁用")
    private String status;
}