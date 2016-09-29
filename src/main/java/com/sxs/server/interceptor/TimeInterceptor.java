package com.sxs.server.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 执行时间方法拦截器
 */
public class TimeInterceptor implements MethodInterceptor {
    public static final Logger logger = LoggerFactory.getLogger(TimeInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        StopWatch clock = new StopWatch();
        clock.start(); //计时开始
        //监控的类名
        String className = invocation.getThis().getClass().getSimpleName();
        //监控的方法名
        String methodName = className + "." + invocation.getMethod().getName();

        Object result = invocation.proceed();

        clock.stop(); //计时结束
        logger.debug("[" + methodName + "] 执行时间:" + clock.getTime() + " ms");
        return result;
    }
}
