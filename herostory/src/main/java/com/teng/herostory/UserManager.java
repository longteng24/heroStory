package com.teng.herostory;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: nettyProject
 * @description: 广播员
 * @author: Mr.Teng
 * @create: 2021-01-01 15:47
 **/
public final class UserManager {
    /**
     * 用户字典
     */
    static final Map<Integer, User> _userMap = new ConcurrentHashMap<>();

    /**
     * 工具类
     */
    private UserManager() {
    }
    /**
     * 增加
     * @param u 客户
     */
    public static  void addUser(User u) {
        if (u == null) {
            return;
        }
        // key如果有value值，则不操作。无值才存入  (为防止并发，解决冲突)
        _userMap.putIfAbsent(u.getUserId(), u);
    }
    /**
     * 增加
     * @param userId 客户id
     */
    public static  void removeByUserId(int userId) {

        // key如果有value值，则不操作。无值才存入  (为防止并发，解决冲突)
        _userMap.remove(userId);
    }

    /**
     *用户列表
     */
    public static Collection<User> listUser() {

      return _userMap.values();
    }
}
