package com.hxy.controller;

import com.hxy.pojo.Comment;
import com.hxy.result.ResponseResult;
import com.hxy.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhangyadong
 * @version 1.0
 * @ClassName CommentController
 * @date 2026-04-09 9:53
 */

@RestController
@RequestMapping("/comment")
public class CommentController {


    @Autowired
    private CommentService commentService;

    @RequestMapping("/commentList")
    public ResponseResult commentList(@RequestParam(required = false,value = "articleId") Long articleId,
                                      @RequestParam(required = false,value = "pageNum") Integer pageNum,
                                      @RequestParam(required = false,value = "pageSize") Integer pageSize){
        return commentService.commentList(articleId,pageNum,pageSize);
    }


    @PostMapping
    public ResponseResult addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }
}
