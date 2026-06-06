package com.hxy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 新增博文DTO（接收前端提交的博文数据）
 * @Author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddArticleDto {
    /** 博文ID（新增时为空，编辑时传值） */
    private Long id;
    /** 博文标题 */
    private String title;
    /** 博文内容（支持Markdown） */
    private String content;
    /** 博文摘要 */
    private String summary;
    /** 所属分类ID */
    private Long categoryId;
    /** 缩略图链接 */
    private String thumbnail;
    /** 是否置顶（0=否，1=是） */
    private String isTop;
    /** 状态（0=已发布，1=草稿） */
    private String status;
    /** 访问量（默认0） */
    private Long viewCount;
    /** 是否允许评论（1=是，0=否） */
    private String isComment;
    /** 关联的标签ID列表 */
    private List<Long> tags;
}