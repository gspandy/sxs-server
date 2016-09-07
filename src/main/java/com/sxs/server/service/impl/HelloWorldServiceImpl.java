package com.sxs.server.service.impl;

import com.sxs.server.annotation.ThriftService;
import com.sxs.server.service.HelloWorldService;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by g.h on 2016/9/7.
 */
@ThriftService
public class HelloWorldServiceImpl implements HelloWorldService.Iface {
    public static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldServiceImpl.class);

    @Override
    public String sayHello(String username) throws TException {
        String result = "Hi, " + username + " welcome!";
        LOGGER.debug("参数:[{}],结果:[{}]", username, result);
        return result;
    }
}
