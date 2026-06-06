package com.hxy.service;

import com.hxy.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hxy.result.ResponseResult;

/**
* @author zhangyadong
* @description 针对表【comment(评论表)】的数据库操作Service
* @createDate 2026-04-09 09:49:31
*/
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
