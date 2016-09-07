package com.sxs.server.thrift;

import com.sxs.server.utils.ThriftUtil;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * thrift客户端调用构建工具
 */
public class ThriftClientHelper {
    private TTransport transport;
    private TProtocol protocol;

    public ThriftClientHelper(String host, int port) {
        // 对于非阻塞服务，需要使用TFramedTransport，它将数据分块发送
        this.transport = new TFramedTransport(new TSocket(host, port));
        protocol = new TBinaryProtocol(transport);
    }

    public ThriftClientHelper(TTransport transport) {
        this.transport = transport;
        protocol = new TBinaryProtocol(transport);
    }

    @SuppressWarnings("unchecked")
    public <T> T build(Class<T> thriftClientClass) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // 获取实例化对象
        Class<?> parentClass = ThriftUtil.getParentClass4Client(thriftClientClass);
        TProtocol tMultiplexedProtocol = new TMultiplexedProtocol(protocol, ThriftUtil.getDefaultName(parentClass));
        T thriftClient = ThriftUtil.newInstance(thriftClientClass, tMultiplexedProtocol);
        // 获取iface接口
        Class<?> ifaceClass = ThriftUtil.getThriftServiceIfaceClass(thriftClientClass);
        Object proxy = Proxy.newProxyInstance(thriftClientClass.getClassLoader(), new Class[]{ifaceClass}, new ThriftProxyHandler(thriftClient));
        return (T) proxy;
    }

    /**
     * thrift代理处理类
     */
    public static class ThriftProxyHandler implements InvocationHandler {
        private Object target;

        public ThriftProxyHandler(Object target) {
            this.target = target;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object ret = method.invoke(target, args);
            return ret;
        }
    }

    /**
     * 打开传输通道
     *
     * @throws TTransportException
     */
    public void open() throws TTransportException {
        if (transport != null) {
            transport.open();
        }
    }

    /**
     * 关闭传输通道
     */
    public void close() {
        if (transport != null) {
            transport.close();
        }
    }


}
