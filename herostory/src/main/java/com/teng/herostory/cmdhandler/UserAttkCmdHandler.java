package com.teng.herostory.cmdhandler;

import com.google.protobuf.GeneratedMessageV3;
import com.teng.herostory.GameMsgDecoder;
import com.teng.herostory.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: nettyProject
 * @description: 用户攻击命令出来器
 * @author: Mr.Teng
 * @create: 2021-01-02 10:50
 **/
public class UserAttkCmdHandler implements ICmdHandler< GameMsgProtocol.UserAttkCmd> {
    static final Logger LOGGER = LoggerFactory.getLogger(UserAttkCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserAttkCmd cmd) {
        LOGGER.info("userAttk");

    }
}
