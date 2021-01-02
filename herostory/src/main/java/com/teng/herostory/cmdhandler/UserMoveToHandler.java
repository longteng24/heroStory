package com.teng.herostory.cmdhandler;

import com.teng.herostory.Broadcaster;
import com.teng.herostory.model.MoveState;
import com.teng.herostory.model.User;
import com.teng.herostory.model.UserManager;
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
        //获取移动用户
        User moveUser = UserManager.getUserById(userId);

        if (null == moveUser) {
            return;
        }

        MoveState myState = moveUser.getMoveState();
        myState.fromPosX = cmd.getMoveFromPosX();
        myState.fromPosY = cmd.getMoveFromPosY();
        myState.toPosX = cmd.getMoveToPosX();
        myState.toPosY = cmd.getMoveToPosY();
        myState.startTime = System.currentTimeMillis();

        // 用户移动
        GameMsgProtocol.UserMoveToResult.Builder resultBuilder = GameMsgProtocol.UserMoveToResult.newBuilder();
        resultBuilder.setMoveUserId(userId);
        resultBuilder.setMoveFromPosX(myState.fromPosX);
        resultBuilder.setMoveFromPosY(myState.fromPosY);
        resultBuilder.setMoveToPosX(myState.toPosX);
        resultBuilder.setMoveToPosY(myState.toPosY);
        resultBuilder.setMoveStartTime(myState.startTime);


        GameMsgProtocol.UserMoveToResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }

}
