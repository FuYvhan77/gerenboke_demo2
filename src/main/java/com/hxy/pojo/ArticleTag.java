package com.hxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 文章标签关联表
 * @TableName article_tag
 */
@TableName(value ="article_tag")
@Data
public class ArticleTag {
    /**
     * 文章id
     */

    private Long articleId;

    /**
     * 标签id
     */

    private Long tagId;
}