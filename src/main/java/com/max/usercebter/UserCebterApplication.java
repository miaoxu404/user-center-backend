package com.max.usercebter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.max.usercebter.mapper")
@ComponentScan("com.max.usercebter")
public class UserCebterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCebterApplication.class, args);
    }

}
