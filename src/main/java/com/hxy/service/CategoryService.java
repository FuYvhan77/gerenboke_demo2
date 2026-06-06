package com.hxy.service;

import com.hxy.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hxy.result.ResponseResult;

/**
* @author zhangyadong
* @description 针对表【category(分类表)】的数据库操作Service
* @createDate 2026-04-07 13:46:50
*/
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult getList();
}
