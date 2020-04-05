package com.ushill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.ushill.mapper")
public class Application {

    public static ConfigurableApplicationContext getCtx() {
        return ctx;
    }
    private static ConfigurableApplicationContext ctx;
    public static void main(String[] args) {
        ctx = SpringApplication.run(Application.class, args);
    }
}
