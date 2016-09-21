package com.sxs.server.thrift;

import com.sxs.server.annotation.ThriftService;
import com.sxs.server.exception.ThriftException;
import com.sxs.server.service.ThriftServerAddressRegister;
import com.sxs.server.utils.LocalIpResolve;
import com.sxs.server.utils.ThriftUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.net.SocketException;
import java.util.Map;

/**
 * thrift服务类
 */
@Component
public class ThriftServiceServer implements ApplicationContextAware, ApplicationListener<ContextClosedEvent> {
    public static final Logger logger = LoggerFactory.getLogger(ThriftServiceServer.class);

    private ApplicationContext applicationContext;

    private TServer tServer;

    @Value("${thrift.port}")
    private int port = 9190;

    @Value("${thrift.ip}")
    private String ipString;

    @Value("${thrift.selectorThreads}")
    private int selectorThreads = 4;

    @Value("${thrift.workerThreads}")
    private int workerThreads = 32;

    /**
     * 服务注册
     */
    @Autowired
    private ThriftServerAddressRegister thriftServerAddressRegister;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void start() throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("扫描thrift组件");
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
                .selectorThreads(selectorThreads)
                .workerThreads(workerThreads)
                .processor(multiplexedProcessor));
        String serviceName = null;
        for (Map.Entry<String, Object> entry : thriftServices.entrySet()) {
            Object serviceBean = entry.getValue();
            // 获取processor
            TProcessor processor = ThriftUtils.buildProcessor(serviceBean);
            // 注册
            serviceName = ThriftUtils.getDefaultName(ThriftUtils.getParentClass(serviceBean.getClass()));
            if (logger.isDebugEnabled()) {
                logger.debug("注册服务组件：" + serviceName + "," + processor.getClass());
            }
            multiplexedProcessor.registerProcessor(serviceName, processor);
        }
        tServer.setServerEventHandler(new ThriftServerEventHandler());
        //启动一个新的线程提供服务
        new Thread(() -> tServer.serve()).start();

        while (!tServer.isServing()) {
            Thread.sleep(1);
        }

        logger.debug("启动服务，监听端口：" + port);
        String address = buildLocalAddress();
        // 注册服务
        if (thriftServerAddressRegister != null) {
            thriftServerAddressRegister.register(serviceName, address);
        }

    }

    /**
     * 根据配置文件和动态解析查找ip
     *
     * @return
     * @throws SocketException
     */
    private String buildLocalAddress() throws SocketException {
        String localIp = LocalIpResolve.get();
        if (StringUtils.isNotBlank(ipString) && !ipString.contains(localIp)) {
            throw new ThriftException("ip地址异常");
        }
        return localIp + ":" + port;
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


    public int getPort() {
        return port;
    }

    public int getSelectorThreads() {
        return selectorThreads;
    }

    public void setSelectorThreads(int selectorThreads) {
        this.selectorThreads = selectorThreads;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }
}
