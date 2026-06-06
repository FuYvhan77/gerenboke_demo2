package com.hxy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxy.pojo.ArticleTag;
import com.hxy.service.ArticleTagService;
import com.hxy.mapper.ArticleTagMapper;
import org.springframework.stereotype.Service;

/**
* @author zhangyadong
* @description 针对表【article_tag(文章标签关联表)】的数据库操作Service实现
* @createDate 2026-04-15 13:59:31
*/
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
    implements ArticleTagService{

}




