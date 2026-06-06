package com.hxy.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangyadong
 * @version 1.0
 * @ClassName Stu
 * @date 2026-04-15 14:28
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stu {
    @ExcelProperty("name")
    private String name;
    @ExcelProperty("age")
    private Integer age;
}
