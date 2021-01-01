package com.teng.herostory.cmdhandler;

import com.teng.herostory.Broadcaster;
import com.teng.herostory.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * @program: nettyProject
 * @description:
 * @author: Mr.Teng
 * @create: 2021-01-01 20:41
 **/
public class UserMoveToHandler implements ICmdHandler<GameMsgProtocol.UserMoveToCmd> {
    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserMoveToCmd cmd) {
        if (ctx == null || cmd == null) {
            return;
        }
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId) {
            return;
        }
        // 用户移动
        GameMsgProtocol.UserMoveToResult.Builder resultBuilder = GameMsgProtocol.UserMoveToResult.newBuilder();
        resultBuilder.setMoveUserId(userId);
        resultBuilder.setMoveToPosX(cmd.getMoveToPosX());
        resultBuilder.setMoveToPosY(cmd.getMoveToPosY());
        GameMsgProtocol.UserMoveToResult newResult = resultBuilder.build();
        Broadcaster.Broadcast(newResult);
    }

}
