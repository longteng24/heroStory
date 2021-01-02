package com.teng.herostory.cmdhandler;

import com.teng.herostory.model.MoveState;
import com.teng.herostory.model.User;
import com.teng.herostory.model.UserManager;
import com.teng.herostory.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;

/**
 * @program: nettyProject
 * @description:
 * @author: Mr.Teng
 * @create: 2021-01-01 20:38
 **/
public class WhoElseIsHereCmdHandler implements ICmdHandler<GameMsgProtocol.WhoElseIsHereCmd>{
    @Override
    public void handle(ChannelHandlerContext ctx,GameMsgProtocol.WhoElseIsHereCmd cmd) {
        if (ctx == null || cmd == null) {
            return;
        }
        GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();

        for (User curUser : UserManager.listUser()) {
            if (null == curUser) {
                continue;
            }
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder =
                    GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
            userInfoBuilder.setUserId(curUser.getUserId());
            userInfoBuilder.setHeroAvatar(curUser.getHeroAvatar());

            //获取移动状态
            MoveState myState = curUser.getMoveState();
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.Builder
                    myStateBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.newBuilder();
            myStateBuilder.setFromPosX(myState.fromPosX);
            myStateBuilder.setFromPosY(myState.fromPosY);
            myStateBuilder.setToPosX(myState.toPosX);
            myStateBuilder.setToPosY(myState.toPosY);
            myStateBuilder.setStartTime(myState.startTime);
            //将移动状态，设置到用户中
            userInfoBuilder.setMoveState(myStateBuilder);


            resultBuilder.addUserInfo(userInfoBuilder);

        }
        // 构建结束并广播
        GameMsgProtocol.WhoElseIsHereResult result = resultBuilder.build();
        ctx.writeAndFlush(result);
    }

}
