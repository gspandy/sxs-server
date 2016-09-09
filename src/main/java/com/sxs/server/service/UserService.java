package com.sxs.server.service;

/**
 * 用户服务接口类
 */
public interface UserService {
    /**
     * 分布式事物正常执行测试
     * mybatis和jpa能够正常插入数据
     */
    public void jtaWithSuccess();

    /**
     * 分布式事物回滚测试
     * mybatis 或者jpa插入一条重复数据，引起重复主键异常，测试回滚情况
     */
    public void jtaWithFail();
}
