package com.sxs.server;

import com.sxs.server.service.HelloWorldService;
import com.sxs.server.thrift.ThriftClientHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * thrift通讯客户端入口程序
 */
public class ClientMain {

    public static final Logger LOGGER = LoggerFactory.getLogger(ClientMain.class);

    public static final String DEFAULT_THRIFT_CONFIG_PATH = "/thrift-config.properties";


    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, TException, IOException {
        ExecutorService executor = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 10000; i++) {
            executor.execute(() -> {
                try {
                    ClientMain.communicateWithServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void communicateWithServer() throws IOException, TException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        long begin = System.currentTimeMillis();
        ThriftClientHelper builder = new ThriftClientHelper("localhost", getPort());
        HelloWorldService.Iface helloServiceClient = (HelloWorldService.Iface) builder.build(HelloWorldService.Client.class);
        builder.open();
        for (int i = 0; i < 100; i++) {
            LOGGER.info(helloServiceClient.sayHello("gaohuan" + i));
        }
        builder.close();
        LOGGER.info("执行时间:{}", System.currentTimeMillis() - begin);
    }

    public static int getPort() throws IOException {
        int port = 9090;
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(DEFAULT_THRIFT_CONFIG_PATH));
            String thriftPort = properties.getProperty("thrift.port");
            if (StringUtils.isNotBlank(thriftPort)) {
                port = Integer.valueOf(thriftPort);
            }
        } catch (Exception e) {
            LOGGER.error("加载端口异常!", e);
        }
        return port;
    }

}
