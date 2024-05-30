package com.rainlf.weixinmpserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.rainlf.weixinmpserver.dao.mapper")
public class WeixinMpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeixinMpServerApplication.class, args);
    }

}
