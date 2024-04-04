package com.kangtao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.kangtao.mapper")
public class KangtaoFrameworkApplication {
    public static void main(String[] args) {
        SpringApplication.run(KangtaoFrameworkApplication.class,args);
    }
}