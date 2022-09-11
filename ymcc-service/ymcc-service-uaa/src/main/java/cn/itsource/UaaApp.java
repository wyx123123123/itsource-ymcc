package cn.itsource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
// @MapperScan("cn.itsource.mapper") 因为mybatis-plus我们需要写一个配置类，所以我们直接写在配置类上面
public class UaaApp {

    public static void main(String[] args) {
        SpringApplication.run(UaaApp.class, args);
    }

}
