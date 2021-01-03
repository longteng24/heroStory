package com.teng.herostory.util;

import com.teng.herostory.GameMsgDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @program: nettyProject
 * @description: redis操作工具类
 * @author: Mr.Teng
 * @create: 2021-01-03 09:56
 **/
public final  class RedisUtil {
    static  final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

    private static JedisPool _jedisPool=null;

    /**
     * 私有化构造器
     */
    private RedisUtil() {
    }

    /**
     * 初始化
     */
    public static void init() {
        try {
            _jedisPool = new JedisPool("121.36.28.218", 6379);
            LOGGER.info("Redis 连接成功");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
    }

    /**
     * 获取Redis实例
     * @return  Redis实例
     */
    public static Jedis getJedis() {
        if (null == _jedisPool) {
            throw new RuntimeException("_jedisPool 尚未初始化");
        }
        Jedis jedis = _jedisPool.getResource();
        jedis.auth("tengteng");
        return jedis;
    }
}
