package com.teng.herostory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: nettyProject
 * @description: 服务端消息处理
 * @author: Mr.Teng
 * @create: 2021-01-01 10:50
 **/
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {
    static  final Logger LOGGER = LoggerFactory.getLogger(GameMsgHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        LOGGER.info("收到客户端消息,msg={}",msg);
    }
}
