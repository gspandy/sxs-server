package com.sxs.server;

import com.sxs.server.thrift.ThriftServiceServer;
import com.sxs.server.zookeeper.ZkClientFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * thrift通讯服务端入口程序
 */
@Configuration
@ComponentScan(basePackages = "com.sxs.server")
@PropertySource(value = "classpath:app-config.properties", ignoreResourceNotFound = true)
public class ServerMain {

    /**
     * 资源文件读取配置
     * <p>
     * To resolve ${} in @Values, you must register a static PropertySourcesPlaceholderConfigurer
     * in either XML or annotation configuration file.
     *
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    static ZkClientFactoryBean curatorFrameworkFactory() throws Exception {
        return new ZkClientFactoryBean();
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ServerMain.class);
        ThriftServiceServer thriftServiceServer = applicationContext.getBean(ThriftServiceServer.class);
        thriftServiceServer.start();
    }
}
