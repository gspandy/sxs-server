package com.sxs.server.repositories;

import com.sxs.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by g.h on 2016/9/8.
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
