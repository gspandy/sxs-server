package com.sxs.server.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * thrift服务实现标识注解
 */
@Component
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ThriftService {

}
