package com.sxs.server.dao;


import com.sxs.server.annotation.MybatisDao;
import com.sxs.server.model.User;

@MybatisDao
public interface UserDao {
    int deleteByPrimaryKey(String uid);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String uid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}