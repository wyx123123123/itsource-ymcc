package cn.itsource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
// @MapperScan("cn.itsource.mapper") 因为mybatis-plus我们需要写一个配置类，所以我们直接写在配置类上面
public class MediaApp {

    public static void main(String[] args) {
        SpringApplication.run(MediaApp.class, args);
    }

}
