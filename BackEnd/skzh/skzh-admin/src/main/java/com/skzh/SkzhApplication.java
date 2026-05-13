package com.skzh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 启动程序
 *
 * @author skzh
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableCaching  //开启缓存注解功能  Spring Cache
public class SkzhApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(SkzhApplication.class, args);
        System.out.println("******************* 启动成功 ************************" );
    }
}
