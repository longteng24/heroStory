package com.teng.herostory;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.io.Resources;


/**
 * @program: nettyProject
 * @description: Mysql会话工厂
 * @author: Mr.Teng
 * @create: 2021-01-02 15:30
 **/
public final class MySqlSessionFactory {

    /**
     * 日志对象
     */
    static private final Logger LOGGER = LoggerFactory.getLogger(MySqlSessionFactory.class);

    /**
     * MyBatis Sql 会话工厂
     */
    static private SqlSessionFactory _sqlSessionFactory;

    private MySqlSessionFactory() {
    }

    /**
     * 初始化
     */
    public static void init() {
        try {
            _sqlSessionFactory = (new SqlSessionFactoryBuilder()).build(
                    Resources.getResourceAsStream("MyBatisConfig.xml")
            );

            //测试数据库连接
            SqlSession tempSession = openSession();

            tempSession.getConnection()
                    .createStatement()
                    .execute("SELECT -1");

            tempSession.close();

            LOGGER.info("mysql 数据库连接初始化成功");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
    }

    /**
     * 创建Mysql 会话
     * @return
     */
    public static SqlSession openSession() {
        if (null == _sqlSessionFactory) {
            throw new RuntimeException("_sqlSessionFactory 未初始化");
        }

        return _sqlSessionFactory.openSession(true);
    }
}
