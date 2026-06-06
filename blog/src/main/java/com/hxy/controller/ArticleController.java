package com.hxy.controller;

import com.hxy.result.ResponseResult;
import com.hxy.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){

        ResponseResult result =  articleService.hotArticleList();
        return result;
    }


    @GetMapping("/articleList")
    public ResponseResult articleList(@RequestParam(required = false,value = "pageNum") Integer pageNum,
                                      @RequestParam(required = false,value = "pageSize") Integer pageSize,
                                      @RequestParam(required = false,value = "categoryId") Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }


    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }
}