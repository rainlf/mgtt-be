package com.rainlf.weixin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WeixinMpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeixinMpServerApplication.class, args);
    }

}
