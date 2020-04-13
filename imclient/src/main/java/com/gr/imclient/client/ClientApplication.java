package com.gr.imclient.client;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-08 14:03
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.gr.imclient")
@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args){
        // 启动并初始化 Spring 环境及其各 Spring 组件
        ApplicationContext context = SpringApplication.run(ClientApplication.class,args);

        CommandController commandClient = context.getBean(CommandController.class);
        commandClient.initCommandMap();
        try {
            commandClient.startCommandThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
