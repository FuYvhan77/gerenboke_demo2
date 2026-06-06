package com.hxy.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.hxy.utils.UserContextHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author zhangyadong
 * @version 1.0
 * @ClassName MyMetaObjectHandler
 * @date 2026-04-09 13:57
 */

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Long id = UserContextHolder.getUser().getId();
        this.setFieldValByName("createBy", id, metaObject);
        this.setFieldValByName("updateBy", id, metaObject);
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long id = UserContextHolder.getUser().getId();
        this.setFieldValByName("updateBy", id, metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
