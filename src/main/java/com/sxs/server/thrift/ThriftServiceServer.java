package com.sxs.server.thrift;

import com.sxs.server.annotation.ThriftService;
import com.sxs.server.utils.ThriftUtil;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * thrift服务类
 */
@Component
public class ThriftServiceServer implements ApplicationContextAware, ApplicationListener<ContextClosedEvent> {
    public static final Logger LOGGER = LoggerFactory.getLogger(ThriftServiceServer.class);

    private ApplicationContext applicationContext;
    /**
     * 默认配置文件
     */
    public static final String DEFAULT_THRIFT_CONFIG_PATH = "/thrift-config.properties";
    /**
     * thrift相关配置地址
     */
    private String thriftConfigPath = DEFAULT_THRIFT_CONFIG_PATH;

    private TServer tServer;

    @Value("${thrift.port}")
    private int port = 9090;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void start() throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("扫描thrift组件");
        }
        Map<String, Object> thriftServices = applicationContext.getBeansWithAnnotation(ThriftService.class);
        if (thriftServices == null) {
            return;
        }
        TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(port);
        TFramedTransport.Factory transportFactory = new TFramedTransport.Factory();
        TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();
        /*
            如果服务器采用TNonblockingServer的话，客户端必须采用TFramedTransport
         */
        tServer = new TThreadedSelectorServer(new TThreadedSelectorServer.Args(serverTransport)
                .transportFactory(transportFactory)
                .protocolFactory(new TBinaryProtocol.Factory())
                .selectorThreads(4)
                .workerThreads(32)
                .processor(multiplexedProcessor));

        for (Map.Entry<String, Object> entry : thriftServices.entrySet()) {
            Object serviceBean = entry.getValue();
            // 获取processor
            TProcessor processor = ThriftUtil.buildProcessor(serviceBean);
            // 注册
            String serviceName = ThriftUtil.getDefaultName(ThriftUtil.getParentClass(serviceBean.getClass()));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("注册服务组件：" + serviceName + "," + processor.getClass());
            }
            multiplexedProcessor.registerProcessor(serviceName, processor);
        }

        //启动一个新的线程提供服务
        new Thread(() -> tServer.serve()).start();

        while (!tServer.isServing()) {
            Thread.sleep(1);
        }
        LOGGER.debug("启动服务，监听端口：" + port);
    }

    /**
     * closed event
     */
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        if (tServer != null) {
            tServer.stop();
        }
    }

    public String getThriftConfigPath() {
        return thriftConfigPath;
    }

    public void setThriftConfigPath(String thriftConfigPath) {
        this.thriftConfigPath = thriftConfigPath;
    }

    public int getPort() {
        return port;
    }

}
