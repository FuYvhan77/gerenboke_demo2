package com.hxy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hxy.mapper")
public class OllamaBbbApplication {
    public static void main(String[] args) {
        SpringApplication.run(OllamaBbbApplication.class, args);
    }
}