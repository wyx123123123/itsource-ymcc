package cn.itsource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class})
@EnableDiscoveryClient

//@EnableScheduling   // 2.开启定时任务
//@EnableAsync        // 2.开启多线程
public class UaaApp {

    public static void main(String[] args) {
        SpringApplication.run(UaaApp.class, args);
    }

}
