package com.sxs.server;

import com.sxs.server.thrift.ThriftServiceServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

/**
 * thrift通讯服务端入口程序
 */
public class ServerMain {

    public static void main(String[] args) throws Exception {
        SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application-context.xml");
        ThriftServiceServer thriftServiceServer = applicationContext.getBean(ThriftServiceServer.class);
        thriftServiceServer.start();
    }
}
