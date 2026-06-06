package com.hxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxy.enums.AppHttpCodeEnum;
import com.hxy.exception.SystemException;
import com.hxy.mapper.UserMapper;
import com.hxy.pojo.Comment;
import com.hxy.pojo.SystemConstants;
import com.hxy.pojo.User;
import com.hxy.result.ResponseResult;
import com.hxy.service.CommentService;
import com.hxy.mapper.CommentMapper;
import com.hxy.utils.BeanCopyUtils;
import com.hxy.utils.UserContextHolder;
import com.hxy.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author zhangyadong
 * @description 针对表【comment(评论表)】的数据库操作Service实现
 * @createDate 2026-04-09 09:49:31
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

    @Autowired
    private UserMapper userMapper;


    // ... existing code ...

    /**
     * 获取指定文章的评论列表，支持分页查询
     * <p>
     * 该方法会查询指定文章的根评论（顶级评论）及其子评论，并将子评论组织到对应父评论的children属性中，
     * 形成树形结构返回。返回结果包含评论总数和评论列表。
     * </p>
     *
     * @param articleId 文章ID，用于查询该文章下的所有评论
     * @param pageNum   页码，从1开始，用于分页查询根评论
     * @param pageSize  每页大小，用于分页查询根评论
     * @return ResponseResult 封装了响应结果的统一返回对象
     * - total: 根评论的总记录数
     * - rows: 根评论列表（CommentVo），每个根评论可能包含其子评论列表（children属性）
     */
    @Override
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        // 分页查询根评论（rootId为ROOT_COMMENT的评论），按创建时间降序排列
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, articleId);
        queryWrapper.eq(Comment::getRootId, SystemConstants.ROOT_COMMENT);
        queryWrapper.orderByDesc(Comment::getCreateTime);

        Page<Comment> page = new Page<>(pageNum, pageSize);

        Page<Comment> page1 = page(page, queryWrapper);
        // 将根评论实体列表转换为VO对象列表
        List<CommentVo> commentVosroot = toCommentVoList(page1.getRecords());


        // 查询当前文章的所有子评论（rootId不为ROOT_COMMENT的评论）
        LambdaQueryWrapper<Comment> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Comment::getArticleId, articleId);
        queryWrapper1.ne(Comment::getRootId, SystemConstants.ROOT_COMMENT);
        List<Comment> list = list(queryWrapper1);
        // 将子评论实体列表转换为VO对象列表
        List<CommentVo> commentVos = toCommentVoList(list);


        // 遍历所有根评论和子评论，将子评论按照rootId关联到对应的父评论的children属性中，构建树形结构
        for (CommentVo commentVo : commentVosroot) {
            for (CommentVo vo : commentVos) {
                if (commentVo.getId().equals(vo.getRootId())) {
                    if (commentVo.getChildren() == null) {
                        commentVo.setChildren(new ArrayList<>());
                    }
                    commentVo.getChildren().add(vo);
                }
            }
        }


        // 构建返回结果，包含根评论总数和带有子评论的根评论列表
        Map<String, Object> map = new HashMap<>();
        map.put("total", page1.getTotal());
        map.put("rows", commentVosroot);
        return ResponseResult.okResult(map);
    }



// ... existing code ...


    // ... existing code ...

    /**
     * 将评论实体列表转换为VO对象列表，并填充用户信息
     * <p>
     * 该方法会将Comment实体批量复制为CommentVo对象，并为每个评论VO填充创建者的昵称。
     * 如果评论是回复评论（toCommentId不为-1），还会填充被回复用户的昵称。
     * </p>
     *
     * @param records 评论实体列表，需要转换为VO对象的原始数据
     * @return List<CommentVo> 转换后的评论VO对象列表，已填充用户名和被回复用户名信息
     */
    private List<CommentVo> toCommentVoList(List<Comment> records) {
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(records, CommentVo.class);

        // 遍历评论VO列表，为每个评论填充创建者昵称和被回复用户昵称（如果是回复评论）
        for (CommentVo commentVo : commentVos) {
            User user = userMapper.selectById(commentVo.getCreateBy());
            commentVo.setUsername(user.getNickName());


            // 判断是否为回复评论，如果是则填充被回复用户的昵称
            if (commentVo.getToCommentId() != -1) {
                User toUser = userMapper.selectById(commentVo.getToCommentUserId());
                commentVo.setToCommentUserName(toUser.getNickName());
            }
        }

        return commentVos;
    }






    @Override
    public ResponseResult addComment(Comment comment) {
        // 1. 评论内容非空校验
        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);

        return ResponseResult.okResult();
    }

    // ... existing code ...
}




