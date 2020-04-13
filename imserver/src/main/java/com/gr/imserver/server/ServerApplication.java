package com.gr.imserver.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-13 15:05
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.gr.imserver")
@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args){

        // 启动并初始化 Spring 环境及其各 Spring 组件
        ApplicationContext context =
                SpringApplication.run(ServerApplication.class,args);

        ChatServer nettyServer = context.getBean(ChatServer.class);

        nettyServer.run();
    }

}
