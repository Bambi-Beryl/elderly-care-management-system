package com.neusoft.care;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.neusoft.care.**.mapper")
public class CareApplication {
    public static void main(String[] args) {
        SpringApplication.run(CareApplication.class, args);
    }
}
