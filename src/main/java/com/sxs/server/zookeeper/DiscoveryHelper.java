package com.sxs.server.zookeeper;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.curator.x.discovery.strategies.RandomStrategy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * zookeeper查找注册查找帮助类
 */
public class DiscoveryHelper {
    private static final String CONNECTION_STRING = "localhost:2181";
    private static String PATH = "/sxs-services";
    private static String SERVICE_NAME = "db-operation";
    private static Map<String, ServiceProvider<InstanceDetails>> providers = Maps.newHashMap();
    private static List<RegisterServer> registerServers = Lists.newArrayList();


    public static void main(String[] args) throws Exception {
        final CuratorFramework client = newClient();
        final ServiceDiscovery<InstanceDetails> serviceDiscovery = newServiceDiscovery(client);
        try {
            addInstance(client, serviceDiscovery, SERVICE_NAME, 9091, "localhost");
            addInstance(client, serviceDiscovery, SERVICE_NAME, 9092, "localhost");
            listInstances(serviceDiscovery);
            deleteInstance(SERVICE_NAME, 9091, "localhost");
            listInstances(serviceDiscovery);
            System.out.println("q or quit: Quit the example");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            boolean done = false;
            while (!done) {
                String line = in.readLine();
                if (line.equalsIgnoreCase("q") || line.equalsIgnoreCase("quit")) {
                    done = true;
                }
            }
            System.out.println("done...");
        } finally {
            for (ServiceProvider<InstanceDetails> cache : providers.values()) {
                CloseableUtils.closeQuietly(cache);
            }
            CloseableUtils.closeQuietly(serviceDiscovery);
            CloseableUtils.closeQuietly(client);
        }

    }

    /**
     * 随机列表服务实例
     *
     * @param serviceDiscovery
     * @param providers
     * @param serviceName
     * @throws Exception
     */
    private static void listRandomInstance(ServiceDiscovery<InstanceDetails> serviceDiscovery, Map<String, ServiceProvider<InstanceDetails>> providers, String serviceName) throws Exception {
        ServiceProvider<InstanceDetails> provider = providers.get(serviceName);
        if (provider == null) {
            provider = serviceDiscovery.serviceProviderBuilder().serviceName(serviceName).providerStrategy(new RandomStrategy<InstanceDetails>()).build();
            providers.put(serviceName, provider);
            provider.start();
        }

        ServiceInstance<InstanceDetails> instance = provider.getInstance();
        if (instance == null) {
            System.err.println("No instances named: " + serviceName);
        } else {
            outputInstance(instance);
        }
    }

    /**
     * 列出所有服务实例
     *
     * @param serviceDiscovery
     * @throws Exception
     */
    private static void listInstances(ServiceDiscovery<InstanceDetails> serviceDiscovery) throws Exception {
        Collection<String> serviceNames = serviceDiscovery.queryForNames();
        System.out.println(serviceNames.size() + " type(s)");
        for (String serviceName : serviceNames) {
            Collection<ServiceInstance<InstanceDetails>> instances = serviceDiscovery.queryForInstances(serviceName);
            System.out.println(serviceName);
            for (ServiceInstance<InstanceDetails> instance : instances) {
                outputInstance(instance);
            }
        }

    }

    /**
     * 输出实例信息
     *
     * @param instance
     */
    private static void outputInstance(ServiceInstance<InstanceDetails> instance) {
        System.out.println("\t" + instance.getPayload().getDescription() + ": " + instance.buildUriSpec());
    }

    /**
     * 删除服务实例
     *
     * @param serviceName
     * @param port
     * @param address
     * @throws Exception
     */
    private static void deleteInstance(final String serviceName, int port, String address) throws Exception {
        RegisterServer registerServer = Iterables.find(
                registerServers,
                new Predicate<RegisterServer>() {
                    @Override
                    public boolean apply(RegisterServer server) {
                        ServiceInstance<InstanceDetails> instance = server.getThisInstance();
                        return (instance.getName().equalsIgnoreCase(serviceName)
                                && instance.getPort() == port
                                && instance.getAddress().equalsIgnoreCase(address));
                    }
                },
                null
        );
        if (registerServer == null) {
            System.err.println("No servers found named: " + serviceName);
            return;
        }
        registerServer.getServiceDiscovery().unregisterService(registerServer.getThisInstance());
        registerServers.remove(registerServer);

        System.out.println("Removed a random instance of: " + serviceName);
    }

    /**
     * 添加一个服务实例
     *
     * @param client
     * @param serviceDiscovery
     * @param serviceName
     * @param port
     * @param address
     * @throws Exception
     */
    private static void addInstance(CuratorFramework client, ServiceDiscovery<InstanceDetails> serviceDiscovery, String serviceName, int port, String address) throws Exception {
        RegisterServer server = new RegisterServer(client, serviceDiscovery, PATH, serviceName, port, address);
        registerServers.add(server);
        System.out.println(serviceName + " - " + address + ":" + port + " added");
    }

    /**
     * 新建一个zookeeper客户端连接
     *
     * @return
     */
    public static CuratorFramework newClient() {
        CuratorFramework client = CuratorFrameworkFactory.newClient(CONNECTION_STRING, new ExponentialBackoffRetry(1000, 3));
        client.start();
        return client;
    }

    /**
     * 新建一个服务查找对象
     *
     * @param client
     * @return
     * @throws Exception
     */
    public static ServiceDiscovery<InstanceDetails> newServiceDiscovery(CuratorFramework client) throws Exception {
        ServiceDiscovery<InstanceDetails> serviceDiscovery = ServiceDiscoveryBuilder.builder(InstanceDetails.class)
                .client(client)
                .basePath(PATH)
                .serializer(new JsonInstanceSerializer<InstanceDetails>(InstanceDetails.class))
                .build();
        serviceDiscovery.start();
        return serviceDiscovery;
    }

}
