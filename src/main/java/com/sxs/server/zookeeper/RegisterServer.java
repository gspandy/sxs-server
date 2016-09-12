package com.sxs.server.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;

/**
 * zookeeper服务注册类
 */
public class RegisterServer {

    private final ServiceDiscovery<InstanceDetails> serviceDiscovery;
    private final ServiceInstance<InstanceDetails> thisInstance;

    public RegisterServer(CuratorFramework client, ServiceDiscovery<InstanceDetails> serviceDiscovery, String path, String serviceName, int port, String address) throws Exception {
        this.serviceDiscovery = serviceDiscovery;
        thisInstance = newServiceInstance(serviceName, port, address);
        serviceDiscovery.registerService(thisInstance);
    }

    public ServiceInstance<InstanceDetails> getThisInstance() {
        return thisInstance;
    }

    public ServiceDiscovery<InstanceDetails> getServiceDiscovery() {
        return serviceDiscovery;
    }

    /**
     * 新建服务实例
     *
     * @param serviceName
     * @param port
     * @param address
     * @return
     * @throws Exception
     */
    private ServiceInstance<InstanceDetails> newServiceInstance(String serviceName, int port, String address) throws Exception {
        return ServiceInstance.<InstanceDetails>builder()
                .name(serviceName)
                .port(port)
                .address(address)
                .payload(new InstanceDetails(serviceName))
                .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                .build();
    }

}
