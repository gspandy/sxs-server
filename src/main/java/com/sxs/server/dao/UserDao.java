package com.sxs.server.dao;


import com.sxs.server.annotation.MybatisDao;
import com.sxs.server.model.User;

/**
 * 用户相关数据操作类 ,mybatis-generator工具生成
 */
@MybatisDao
public interface UserDao {
    /**
     * 根据主键删除
     *
     * @param uid
     * @return
     */
    int deleteByPrimaryKey(String uid);

    /**
     * 插入用户
     *
     * @param record
     * @return
     */
    int insert(User record);

    /**
     * 插入用户
     *
     * @param record
     * @return
     */
    int insertSelective(User record);

    /**
     * 通过主键查询
     *
     * @param uid
     * @return
     */
    User selectByPrimaryKey(String uid);

    /***
     * 通过主键更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * 通过主键更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(User record);
}