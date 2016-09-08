package com.sxs.server.service.impl;

import com.sxs.server.dao.UserDao;
import com.sxs.server.model.User;
import com.sxs.server.repositories.UserRepository;
import com.sxs.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by g.h on 2016/9/8.
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDao userDao;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void save(User user) {
        userRepository.save(user);
//        userDao.insert(user);
    }

}
