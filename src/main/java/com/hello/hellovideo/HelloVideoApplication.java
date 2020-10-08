package com.hello.hellovideo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan
@EnableScheduling
public class HelloVideoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloVideoApplication.class, args);
    }

}
