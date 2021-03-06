package com.sxs.server.service.impl;

import com.sxs.server.dao.UserDao;
import com.sxs.server.po.User;
import com.sxs.server.service.UserService;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户相关服务接口实现
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
    public static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    /**
     * 分布式事物正常执行测试
     * mybatis和jpa能够正常插入数据
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void jtaWithSuccess() {

        User jpaUser = new User();
        jpaUser.setUid(RandomUtils.nextLong(1000, 1000000));
        jpaUser.setUserName("66666");
        jpaUser.setSex("1");
        jpaUser.setPassword("55");

        User mybatisUser = new User();
        mybatisUser.setUid(RandomUtils.nextLong(1000, 1000000));
        mybatisUser.setUserName("66666");
        mybatisUser.setSex("1");
        mybatisUser.setPassword("55");
        //mybatis事物
        userDao.insert(mybatisUser);
    }

    /**
     * 分布式事物回滚测试
     * mybatis 或者jpa插入一条重复数据，引起重复主键异常，测试回滚情况
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void jtaWithFail() {

        User jpaUser = new User();
        jpaUser.setUid(777l);
        jpaUser.setUserName("66666");
        jpaUser.setSex("1");
        jpaUser.setPassword("55");

        User mybatisUser = new User();
        mybatisUser.setUid(8888l);
        mybatisUser.setUserName("66666");
        mybatisUser.setSex("1");
        mybatisUser.setPassword("55");
        //jpa事物
        //mybatis事物
        userDao.insert(mybatisUser);
    }

}
