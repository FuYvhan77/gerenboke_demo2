package com.hxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxy.pojo.Article;
import com.hxy.pojo.Category;
import com.hxy.pojo.SystemConstants;
import com.hxy.result.ResponseResult;
import com.hxy.service.ArticleService;
import com.hxy.service.CategoryService;
import com.hxy.mapper.CategoryMapper;
import com.hxy.utils.BeanCopyUtils;
import com.hxy.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author zhangyadong
* @description 针对表【category(分类表)】的数据库操作Service实现
* @createDate 2026-04-07 13:46:50
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //1,查询文章表
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.eq(Article::getDelFlag, SystemConstants.DELETE_FLAG_NO);
        queryWrapper.groupBy(Article::getCategoryId);
        queryWrapper.select(Article::getCategoryId);
        List<Article> list = articleService.list(queryWrapper);


        //2,获取分类id
        List<Long> categoryIds = list.stream()
                .map(Article::getCategoryId)
                .distinct()
                .collect(Collectors.toList());
        //3,根据分类id查询分类
        List<Category> categories = listByIds(categoryIds);

        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult getList() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, SystemConstants.STATUS_NORMAL);
        List<Category> list = list(queryWrapper);
        return ResponseResult.okResult(list);
    }
}




