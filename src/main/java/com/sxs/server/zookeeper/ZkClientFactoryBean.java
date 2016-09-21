package com.sxs.server.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;

import java.io.Closeable;

/**
 * 获取zookeeper客户端连接
 */
public class ZkClientFactoryBean implements FactoryBean<CuratorFramework>, Closeable {

    @Value("${zookeeper.connectString}")
    private String connectString;

    @Value("${zookeeper.sessionTimeout}")

    private int sessionTimeout = 30000;

    @Value("${zookeeper.connectionTimeout}")
    private int connectionTimeout = 15000;

    private static CuratorFramework zkClient;


    @Override
    public CuratorFramework getObject() throws Exception {
        if (zkClient != null) {
            return zkClient;
        }
        return newClient(connectString, sessionTimeout, connectionTimeout);
    }

    @Override
    public Class<?> getObjectType() {
        return CuratorFramework.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private static CuratorFramework newClient(String connectString, int sessionTimeout, int connectionTimeout) {
        zkClient = CuratorFrameworkFactory.newClient(connectString, sessionTimeout, connectionTimeout, new ExponentialBackoffRetry(1000, Integer.MAX_VALUE));
        zkClient.start();
        return zkClient;
    }

    public void close() {
        if (zkClient != null) {
            zkClient.close();
        }
    }

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

}
