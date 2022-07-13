package com.zimug.courses.security.basic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author maizi
 */
@SpringBootApplication(scanBasePackages = {"com.zimug"})
@MapperScan(basePackages = {"com.zimug"})
public class GithubLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(GithubLoginApplication.class, args);
    }

}
