package com.teng.herostory.cmdhandler;

import com.teng.herostory.Broadcaster;
import com.teng.herostory.model.User;
import com.teng.herostory.model.UserManager;
import com.teng.herostory.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * @program: nettyProject
 * @description: 用户进入时，处理器
 * @author: Mr.Teng
 * @create: 2021-01-01 20:32
 **/
public class UserEntryCmdHandler implements ICmdHandler<GameMsgProtocol.UserEntryCmd>{
    @Override
    public void handle(ChannelHandlerContext ctx,  GameMsgProtocol.UserEntryCmd cmd) {
        if (ctx == null || cmd == null) {
            return;
        }

        Integer userId = (Integer)ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        User curUser = UserManager.getUserById(userId);

        GameMsgProtocol.UserEntryResult.Builder resultBuilder = GameMsgProtocol.UserEntryResult.newBuilder();
        resultBuilder.setUserId(userId);
        resultBuilder.setHeroAvatar(curUser.getHeroAvatar());
        resultBuilder.setUserName(curUser.getUserName());

        // 构建结束并广播
        GameMsgProtocol.UserEntryResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }
}
