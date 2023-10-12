package com.maslov.mvchomework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan("org.springframework.data.redis")
public class MvcHomeworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(MvcHomeworkApplication.class, args);
    }
}
