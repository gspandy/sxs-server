package com.sxs.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.sxs.server.exception.ThriftException;
import com.sxs.server.po.ServiceDescription;
import com.sxs.server.service.ThriftServerAddressRegister;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * thrift server地址注册实现类
 */
@Service("thriftServerAddressRegister")
public class ThriftServerAddressRegisterImpl implements ThriftServerAddressRegister {
    public static final Logger logger = LoggerFactory.getLogger(ThriftServerAddressRegister.class);

    public static final String PATH_SEPARATOR = "/";

    @Value("${zookeeper.basePath}")
    private String basePath = "/sxs-services";

    private CuratorFramework zkClient;

    @Override
    public void register(String service, String address) {
        if (zkClient.getState() == CuratorFrameworkState.LATENT) {
            zkClient.start();
        }
        try {
            //临时节点
            zkClient.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(buildPath(service), buildData(service, address));
        } catch (UnsupportedEncodingException e) {
            logger.error("zookeeper注册服务地址异常:{}", e);
            throw new ThriftException("不支持的注册地址", e);
        } catch (Exception e) {
            logger.error("zookeeper注册服务地址异常:{}", e);
            throw new ThriftException("zookeeper注册服务地址异常:{}", e);
        }
    }

    /**
     * 构建服务路径
     *
     * @param serviceName
     * @return
     */
    private String buildPath(String serviceName) {
        StringBuilder path = new StringBuilder();
        path.append(basePath)
                .append(PATH_SEPARATOR)
                .append(serviceName)
                .append(PATH_SEPARATOR)
                .append(UUID.randomUUID().toString().replaceAll("-", ""));
        return path.toString();
    }

    /**
     * 构建服务节点数据
     *
     * @param serviceName
     * @param address
     * @return
     */
    private byte[] buildData(String serviceName, String address) {
        ServiceDescription serviceDescription = new ServiceDescription(serviceName, address);
        return JSON.toJSONBytes(serviceDescription);
    }


    public void setZkClient(CuratorFramework zkClient) {
        this.zkClient = zkClient;
    }
}
