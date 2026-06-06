package com.hxy.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hxy.pojo.Article;
import com.hxy.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyadong
 * @version 1.0
 * @ClassName TestRunner
 * @date 2026-04-10 9:46
 */

@Component
public class TestRunner implements CommandLineRunner {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    // 项目启动时执行
    @Override
    public void run(String... args) throws Exception {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getId, Article::getViewCount);
        List<Article> list = articleService.list(queryWrapper);


        Map<String, String> map=new HashMap<>();
        for (Article article : list) {
            map.put(String.valueOf(article.getId()), String.valueOf(article.getViewCount()));
        }

        redisTemplate.opsForHash().putAll("article:viewCount", map);
    }
}
