package com.sxs.server;

import com.sxs.server.dao.UserDao;
import com.sxs.server.po.User;
import com.sxs.server.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

/**
 * spring-junit测试类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:application-context.xml", "classpath:application-context.xml"})
public class JtaUseTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;


    @Test
    public void testJdbc() {
        String sql = "INSERT INTO sxs_server.t_user (uid, user_account, phone, user_name, password, email, address, sex) VALUES (?,?,?,?,?,?,?,?)";
        List<String> args = Arrays.asList("1", "1", "15811056271", "gaohuan", "1234", "gaohuan0904@163.com", "xxxxx", "1");
        int r = jdbcTemplate.update(sql, args.toArray());
        Assert.assertEquals(1, r);
    }

    @Test
    public void testMybatis() {
        User user = new User();
        user.setUid("444");
        user.setUserName("test");
        user.setSex("1");
        user.setPassword("4321");
        userDao.insert(user);
    }

    @Test
    public void testJtaWithSuccess() {
        userService.jtaWithSuccess();
    }

    @Test
    public void testJtaWithFail() {
        userService.jtaWithFail();
    }

}
