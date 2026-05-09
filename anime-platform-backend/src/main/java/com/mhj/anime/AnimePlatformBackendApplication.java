package com.mhj.anime;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.mhj.anime.mapper")
@SpringBootApplication
public class AnimePlatformBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnimePlatformBackendApplication.class, args);
    }
}
