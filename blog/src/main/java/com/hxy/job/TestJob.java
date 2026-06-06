package com.hxy.job;


import com.hxy.pojo.Article;
import com.hxy.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TestJob {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 每隔5秒执行一次
    @Scheduled(cron = "0 0/5 * * * ?")
    public void test() {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries("article:viewCount");
        List<Article> articles=new ArrayList<>();

        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            Article article=new Article();
            article.setId(Long.valueOf(entry.getKey().toString()));
            article.setViewCount(Long.valueOf(entry.getValue().toString()));
            articles.add(article);
        }


        articleService.updateBatchById( articles);
    }
}
