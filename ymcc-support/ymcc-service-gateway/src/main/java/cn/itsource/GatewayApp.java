package cn.itsource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @BelongsProject: itsource-ymcc
 * @BelongsPackage: cn.itsource
 * @Author: Director
 * @CreateTime: 2022-08-03  15:50
 * @Description: gateway服务启动类
 * @Version: 1.0
 */
@SpringBootApplication
// 注册到Nacos
@EnableDiscoveryClient
public class GatewayApp {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApp.class, args);
    }

}