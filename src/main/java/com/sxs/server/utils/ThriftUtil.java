package com.sxs.server.utils;

import com.sxs.server.exception.ThriftRuntimeException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TProtocol;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Thrift工具类
 */
public class ThriftUtil {

    public static final String CLIENT_NAME = "$Client";

    public static final String PROCESSOR_NAME = "$Processor";

    public static final String IFACE_NAME = "$Iface";

    /**
     * 获取默认名称，首字母小些
     *
     * @param clas
     * @return
     */
    public static String getDefaultName(Class<? extends Object> clas) {
        String className = clas.getName();
        String tmp[] = className.split("\\.");
        if (tmp.length > 0) {
            String name = tmp[tmp.length - 1];
            if (name.length() == 1) {
                return name.toLowerCase();
            } else {
                return name.substring(0, 1).toLowerCase().concat(name.substring(1));
            }
        }
        return className;
    }


    /**
     * @param service thrift的service对象
     * @return service对象构建的TProcessor
     * @throws Exception
     */
    public static TProcessor buildProcessor(Object service) throws Exception {
        // iface接口
        Class<?> ifaceClass = getThriftServiceIfaceClass(service.getClass());
        if (ifaceClass == null) {
            throw new ThriftRuntimeException("the iface is null");
        }
        // Processor
        Class<TProcessor> processorClass = getThriftServiceProcessorClass(ifaceClass);
        if (processorClass == null) {
            throw new ThriftRuntimeException("the processor is null");
        }
        // constructor
        Constructor<TProcessor> constructor = ClassUtils.getConstructorIfAvailable(processorClass, ifaceClass);
        if (constructor == null) {
            throw new ThriftRuntimeException("the processor constructor is null");
        }
        return constructor.newInstance(service);
    }

    /**
     * 获取实际接口的类对象
     *
     * @param inter
     * @return
     * @throws ThriftRuntimeException
     * @throws SecurityException
     * @throws ClassNotFoundException
     */
    public static Class<?> getParentClass(Class<?> inter) throws ThriftRuntimeException, SecurityException, ClassNotFoundException {
        Class<?> ifaceClass = getThriftServiceIfaceClass(inter);
        if (ifaceClass == null) {
            throw new ThriftRuntimeException("the iface is null");
        }
        Class<?> parentClass = getThriftServiceParent(ifaceClass);
        if (parentClass == null) {
            throw new ThriftRuntimeException("is not thrift ServiceImpl " + inter.getName());
        }
        return parentClass;
    }

    /**
     * 获取对象的Thrift的TProcessor
     *
     * @param ifaceClass 作为服务的对象
     * @return class TProcessor的class
     * @throws ClassNotFoundException
     * @throws SecurityException
     * @throws ThriftRuntimeException
     */
    @SuppressWarnings("unchecked")
    private static Class<TProcessor> getThriftServiceProcessorClass(Class<?> ifaceClass) throws SecurityException, ClassNotFoundException,
            ThriftRuntimeException {
        if (ifaceClass == null) {
            return null;
        }
        Class<?> parentClass = getThriftServiceParent(ifaceClass);
        if (parentClass == null) {
            return null;
        }
        return (Class<TProcessor>) getProcessorClass(parentClass);
    }

    /**
     * 获取父类的子类Processor的Class
     *
     * @param parentClass
     * @return
     */
    private static Class<?> getProcessorClass(Class<?> parentClass) {
        Class<?>[] declaredClasses = parentClass.getDeclaredClasses();
        for (Class<?> declaredClasse : declaredClasses) {
            if (declaredClasse.getName().endsWith(PROCESSOR_NAME)) {
                return declaredClasse;
            }
        }
        return null;
    }

    /**
     * 获取服务实现对象的集成接口class
     *
     * @param serviceClass
     * @return
     */
    public static Class<?> getThriftServiceIfaceClass(Class<?> serviceClass) {
        Class<?>[] interfaceClasses = serviceClass.getInterfaces();
        for (Class<?> interfaceClass : interfaceClasses) {
            if (interfaceClass.getName().endsWith(IFACE_NAME)) {
                return interfaceClass;
            }
        }
        return null;
    }

    /**
     * 获取thrift生成的父类对象class
     *
     * @param ifaceClass 获取服务实现对象的集成接口class
     * @return 获取服务实现类对应的thrift生成的父类对象class
     * @throws SecurityException
     * @throws ClassNotFoundException
     */
    private static Class<?> getThriftServiceParent(Class<?> ifaceClass) throws SecurityException, ClassNotFoundException {
        // 获取父类
        String parentClassName = ifaceClass.getName().substring(0, ifaceClass.getName().indexOf(IFACE_NAME));
        return Class.forName(parentClassName);
    }


    /**
     * 获取父级类
     *
     * @param clazz
     * @return
     * @throws ClassNotFoundException
     */
    public static Class<?> getParentClass4Client(Class<?> clazz) throws ClassNotFoundException {
        // 获取父类
        String parentClassName = clazz.getName().substring(0, clazz.getName().indexOf(CLIENT_NAME));
        return Class.forName(parentClassName);
    }

    public static <T> T newInstance(Class<T> serviceClientClass, TProtocol tProtocol) throws NoSuchMethodException,
            SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<T> constructor = serviceClientClass.getConstructor(TProtocol.class);
        return constructor.newInstance(tProtocol);
    }

}
