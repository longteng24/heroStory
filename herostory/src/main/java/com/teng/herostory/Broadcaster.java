package com.teng.herostory;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @program: nettyProject
 * @description: 广播员
 * @author: Mr.Teng
 * @create: 2021-01-01 15:47
 **/
public final class Broadcaster {
    /**
     * 信道组，这里一定要用static 否则无法实现群发
     */
    static private   final ChannelGroup _channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 工具类
     */
    private Broadcaster() {
    }
    /**
     * 增加
     * @param channel 客户端channel
     */
    static public void addChannel(Channel channel) {
        _channelGroup.add(channel);
    }
    /**
     * 移除
     * @param channel 客户端channel
     */
    static public void removeChannel(Channel channel) {
        _channelGroup.remove(channel);
    }

    /**
     * 广播消息
     * @param msg 消息内容
     */
    static public void broadcast(Object msg) {
        if (null == msg) {
            return;
        }
        _channelGroup.writeAndFlush(msg);
    }
}
