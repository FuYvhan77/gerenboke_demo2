package com.hxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxy.dto.AddArticleDto;
import com.hxy.mapper.ArticleTagMapper;
import com.hxy.mapper.CategoryMapper;
import com.hxy.pojo.Article;
import com.hxy.pojo.ArticleTag;
import com.hxy.pojo.Category;
import com.hxy.pojo.SystemConstants;
import com.hxy.result.ResponseResult;
import com.hxy.service.ArticleService;
import com.hxy.mapper.ArticleMapper;
import com.hxy.service.CategoryService;
import com.hxy.utils.BeanCopyUtils;
import com.hxy.utils.UserContextHolder;
import com.hxy.vo.ArticleDetailVo;
import com.hxy.vo.ArticleListVo;
import com.hxy.vo.HotArticleVo;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangyadong
 * @description 针对表【article(文章表)】的数据库操作Service实现
 * @createDate 2026-04-07 09:50:23
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    @Lazy  //懒加载
    private CategoryService categoryService;


    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public ResponseResult hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.orderByDesc(Article::getViewCount);

        queryWrapper.select(Article::getId, Article::getTitle, Article::getViewCount);

        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);

        queryWrapper.eq(Article::getDelFlag, SystemConstants.DELETE_FLAG_NO);

        queryWrapper.last("limit "+SystemConstants.HOT_ARTICLE_LIMIT);

        List<Article> list = list(queryWrapper);

        //将实体类转换成vo类返回
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(list, HotArticleVo.class);


        for (HotArticleVo hotArticleVo : hotArticleVos) {
            Object viewCount = redisTemplate.opsForHash().get("article:viewCount", hotArticleVo.getId().toString());
            if (viewCount != null) {
                if (viewCount instanceof Long) {
                    hotArticleVo.setViewCount((Long) viewCount);
                } else {
                    hotArticleVo.setViewCount(Long.valueOf(viewCount.toString()));
                }
            } else {
                hotArticleVo.setViewCount(0L);
            }
        }


        return ResponseResult.okResult(hotArticleVos);
    }

    // ... existing code ...

        /**
         * 分页查询文章列表
         * <p>
         * 根据页码、每页条数和分类ID查询文章列表，支持按置顶状态排序。
         * 只查询正常状态且未删除的文章。
         * </p>
         *
         * @param pageNum    页码，从1开始
         * @param pageSize   每页显示的记录数
         * @param categoryId 分类ID，可为null或0表示不限制分类
         * @return ResponseResult 包含分页结果的响应对象，其中：
         *                        - total: 符合条件的总记录数
         *                        - rows: 当前页的文章列表
         */
        @Override
        public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
            LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
            queryWrapper.eq(Article::getDelFlag, SystemConstants.DELETE_FLAG_NO);
            queryWrapper.eq(categoryId != null && categoryId > 0, Article::getCategoryId, categoryId);
            queryWrapper.orderByDesc(Article::getIsTop);


            Page<Article> page = new Page<>(pageNum, pageSize);

            Page<Article> page1 = page(page, queryWrapper);

            List<Article> records = page1.getRecords();
            long total = page1.getTotal();
            //---------查询文章对应分类名称------------------------
            List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(records, ArticleListVo.class);

            List<Category> categories = categoryMapper.selectList(null);
            for (ArticleListVo record : articleListVos) {
                for (Category category : categories) {
                    if (record.getCategoryId()==category.getId()){
                        record.setCategoryName(category.getName());
                    }
                }
            }

            //---------------------------------
            for (ArticleListVo hotArticleVo : articleListVos) {
                Object viewCount = redisTemplate.opsForHash().get("article:viewCount", hotArticleVo.getId().toString());
                if (viewCount != null) {
                    if (viewCount instanceof Long) {
                        hotArticleVo.setViewCount((Long) viewCount);
                    } else {
                        hotArticleVo.setViewCount(Long.valueOf(viewCount.toString()));
                    }
                } else {
                    hotArticleVo.setViewCount(0L);
                }
            }
            //---------------------------------

            Map<String, Object> map=new HashMap<>();
            map.put("total",total);
            map.put("rows",articleListVos);


            return ResponseResult.okResult( map);
        }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //查询文章详情
        Article byId = getById(id);
        //封装成VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(byId, ArticleDetailVo.class);
        // 查询文章分类名称
        Category category = categoryMapper.selectById(byId.getCategoryId());
        articleDetailVo.setCategoryName(category.getName());

        Object viewCount = redisTemplate.opsForHash().get("article:viewCount", articleDetailVo.getId().toString());
        if (viewCount != null) {
            if (viewCount instanceof Long) {
                articleDetailVo.setViewCount((Long) viewCount);
            } else {
                articleDetailVo.setViewCount(Long.valueOf(viewCount.toString()));
            }
        } else {
            articleDetailVo.setViewCount(0L);
        }


        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisTemplate.opsForHash().increment("article:viewCount", id.toString(), 1);
        return ResponseResult.okResult();
    }


    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
   @Transactional(rollbackFor = Exception.class)
    public ResponseResult add(AddArticleDto addArticleDto) {
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        article.setCreateBy(UserContextHolder.getUser().getId());
        article.setUpdateBy(UserContextHolder.getUser().getId());
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        System.out.println("插入文章之前："+article.getId());
        save(article);
        System.out.println("插入文章之后："+article.getId());



        List<Long> tags = addArticleDto.getTags();
        for (Long tagId : tags) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(article.getId());
            articleTag.setTagId(tagId);
            articleTagMapper.insert(articleTag);
        }

        //去redis新的博文浏览量
        redisTemplate.opsForHash().put("viewCount1", article.getId().toString(), "0");

        return ResponseResult.okResult();
    }

    // ... existing code ...
}




