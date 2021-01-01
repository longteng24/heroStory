package com.teng.herostory.cmdhandler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;

/**
 * @program: nettyProject
 * @description:  命令处理器接口
 * @author: Mr.Teng
 * @create: 2021-01-01 20:47
 **/
public interface ICmdHandler<TCmd extends GeneratedMessageV3> {

    /**
     * 处理命令
     * @param ctx
     * @param cmd
     */
    void handle(ChannelHandlerContext ctx, TCmd cmd);
}
