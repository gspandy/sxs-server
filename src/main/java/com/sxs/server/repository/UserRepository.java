package com.sxs.server.repository;

import com.sxs.server.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * spring jpa repository类，由spring代理类自动实现
 */
public interface UserRepository extends JpaRepository<User, String> {
    /**
     * 根据uid查询用户
     *
     * @param uid
     * @return
     */
    public User findByUid(String uid);

}
