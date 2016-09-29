package com.sxs.server;

import com.sxs.server.thrift.ThriftClientHelper;
import com.sxs.server.thrift.gen.BaseHeader;
import com.sxs.server.thrift.gen.Direction;
import com.sxs.server.thrift.gen.OperationResult;
import com.sxs.server.thrift.gen.SqlCallParameter;
import com.sxs.server.thrift.service.SqlCallService;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

/**
 * thrift通讯客户端入口程序
 */
public class ClientMain {

    public static final Logger logger = LoggerFactory.getLogger(ClientMain.class);

    public static final String DEFAULT_THRIFT_CONFIG_PATH = "/app-config.properties";


    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, TException, IOException {
//        ExecutorService executor = Executors.newFixedThreadPool(50);
//        for (int i = 0; i < 10; i++) {
//            executor.execute(() -> {
//                try {
//                    ClientMain.communicateWithServer();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        }
        ClientMain.communicateWithServer();
        System.exit(0);

    }

    public static void communicateWithServer() throws IOException, TException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        long begin = System.currentTimeMillis();
        ThriftClientHelper builder = new ThriftClientHelper("localhost", getPort());
        SqlCallService.Iface sqlCallServiceClient = builder.build(SqlCallService.Client.class);
        builder.open();
        //调用远程服务
        BaseHeader baseHeader = new BaseHeader("1.0.0", Direction.REQ, UUID.randomUUID().toString(), "127.0.0.1", 9091);
        //insert
        SqlCallParameter sqlCallParameter = new SqlCallParameter(baseHeader, "select * from  vault_user  where id=291", new ArrayList<>());
        OperationResult operationResult = sqlCallServiceClient.selectSql(sqlCallParameter);
        logger.info(operationResult.toString());

        builder.close();
        logger.info("执行时间:{}", System.currentTimeMillis() - begin);
    }

    public static int getPort() throws IOException {
        int port = 9091;
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(DEFAULT_THRIFT_CONFIG_PATH));
            String thriftPort = properties.getProperty("thrift.port");
            if (StringUtils.isNotBlank(thriftPort)) {
                port = Integer.valueOf(thriftPort);
            }
        } catch (Exception e) {
            logger.error("加载端口异常!", e);
        }
        return port;
    }

}
