package com.sxs.server.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志记录方法拦截器
 */
public class LogInterceptor implements MethodInterceptor {
    protected static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String className = invocation.getThis().getClass().getSimpleName();
        String methodName = className + "." + invocation.getMethod().getName();
        Object[] arguments = invocation.getArguments();
        //过滤属性
        PropertyFilter propertyFilter = new PropertyFilter() {
            @Override
            public boolean apply(Object object, String name, Object value) {
                if (name.startsWith("set") || name.startsWith("parameters")) {
                    return false;
                }
                return true;
            }
        };
        logger.info("[" + methodName + " 参数] {}", JSON.toJSONString(arguments, propertyFilter));

        Object result = invocation.proceed();

        logger.info("[" + methodName + " 结果] {}", JSON.toJSONString(result, propertyFilter));

        return result;
    }

}
