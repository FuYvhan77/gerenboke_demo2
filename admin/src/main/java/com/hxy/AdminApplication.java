package com.hxy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // 声明SpringBoot应用核心注解
@MapperScan("com.hxy.mapper") // 扫描MyBatis Mapper接口包
public class AdminApplication {
    public static void main(String[] args) {
        // 启动SpringBoot应用
        SpringApplication.run(AdminApplication.class, args);
    }
}