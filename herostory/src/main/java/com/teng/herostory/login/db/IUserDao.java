package com.teng.herostory.login.db;

/**
 * @program: nettyProject
 * @description:
 * @author: Mr.Teng
 * @create: 2021-01-02 15:25
 **/
public interface IUserDao {
    /**
     * 根据用户名称获取实体
     *
     * @param userName 用户名称
     * @return
     */
    UserEntity getByUserName(String userName);

    /**
     * 添加用户实体
     *
     * @param newEntity 用户实体
     */
    void insertInto(UserEntity newEntity);
}

