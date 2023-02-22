package com.findPartner;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.findPartner.mapper")
@EnableScheduling
public class FindPartnerBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindPartnerBackendApplication.class, args);
    }

}
