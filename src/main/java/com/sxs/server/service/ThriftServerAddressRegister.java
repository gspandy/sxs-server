package com.sxs.server.service;

/**
 * thrift server地址注册接口
 */
public interface ThriftServerAddressRegister {
    /**
     * 发布服务接口
     *
     * @param service 服务接口名称，一个产品中不能重复
     * @param address 服务发布的地址和端口
     */
    void register(String service, String address);
}
