package com.hxy.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.hxy.pojo.Category;
import com.hxy.pojo.Stu;
import com.hxy.result.ResponseResult;
import com.hxy.service.CategoryService;
import com.hxy.utils.BeanCopyUtils;
import com.hxy.vo.ExcelCategoryVo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/content/category/")
public class EasyExcelController {

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("分类表格", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类")
                    .doWrite(data());
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            ResponseResult result = ResponseResult.errorResult(500, "下载表格错误");
            response.getWriter().println(JSON.toJSONString(result));
        }
    }

    @Autowired
    private CategoryService categoryService;

    private Collection<?> data() {
        List<Category> list = categoryService.list();
        List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(list, ExcelCategoryVo.class);
        return excelCategoryVos;

    }

}
