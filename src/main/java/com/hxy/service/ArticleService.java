package com.hxy.service;

import com.hxy.dto.AddArticleDto;
import com.hxy.pojo.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hxy.result.ResponseResult;

/**
* @author zhangyadong
* @description 针对表【article(文章表)】的数据库操作Service
* @createDate 2026-04-07 09:50:23
*/
public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto addArticleDto);
}
