package com.teng.herostory.login.db;

import com.sun.org.apache.regexp.internal.RE;
import com.teng.herostory.GameMsgDecoder;
import com.teng.herostory.MySqlSessionFactory;
import com.teng.herostory.async.AsyncOperationProcessor;
import com.teng.herostory.async.IAsyncOperation;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 * @program: nettyProject
 * @description:
 * @author: Mr.Teng
 * @create: 2021-01-02 15:54
 **/
public final class LoginService {
    /**
     * 日志对象
     */
    static  final Logger LOGGER = LoggerFactory.getLogger(GameMsgDecoder.class);

    /**
     * 单例对象
     */
    private static final LoginService _instance = new LoginService();

    /**
     * 私有化构造器
     */
    private LoginService() {
    }

    /**
     * 获取单例对象
     * @return
     */
    public static LoginService getInstance() {
        return _instance;
    }

    public void userLogin(String userName, String password, Function<UserEntity,Void> callback) {
        if (null == userName ||
                null == password) {
            return ;
        }
        AsyncGetUserEntity asyncOp = new AsyncGetUserEntity(userName, password) {

            @Override
            public void doFinish() {
                if (null != callback) {
                    callback.apply(this.getUserEntity());
                }

            }
        };
        AsyncOperationProcessor.getInstance().process(asyncOp);

    }

    private class AsyncGetUserEntity implements IAsyncOperation {

        /**
         * 用户名称
         */
        private final String userName;

        /**
         * 用户密码
         */
        private final String password;

        /**
         * 用户实体
         */
        private UserEntity _userEntity;

        /**
         * 获取用户实体
         * @return
         */
        public UserEntity getUserEntity() {
            return _userEntity;
        }

        public AsyncGetUserEntity(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        @Override
        public void doAsync() {
            //安全写法，自动关闭连接
            try (SqlSession mySqlSession = MySqlSessionFactory.openSession()) {
                //获取 dao
                IUserDao dao = mySqlSession.getMapper(IUserDao.class);
                //获取用户实体
                UserEntity userEntity = dao.getByUserName(userName);

                LOGGER.info("当前线程={}",Thread.currentThread().getName());
                //todo 单线程计算，多线程io

                if (userEntity != null) {
                    if (password.equals(userEntity.getPassword())) {
                        throw new RuntimeException("密码错误");
                    }
                } else {
                    userEntity = new UserEntity();
                    userEntity.setUserName(userName);
                    userEntity.setPassword(password);
                    userEntity.setHeroAvatar("Hero_Shaman");

                    dao.insertInto(userEntity);
                }

               _userEntity = userEntity;
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
