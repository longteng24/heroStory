package com.teng.herostory;

import com.teng.herostory.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: nettyProject
 * @description: 服务端消息处理
 * @author: Mr.Teng
 * @create: 2021-01-01 10:50
 **/
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {
    static  final Logger LOGGER = LoggerFactory.getLogger(GameMsgHandler.class);

    /**
     * 信道组，这里一定要用static 否则无法实现群发
     */
    static  final ChannelGroup _channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    static final Map<Integer, User> _userMap = new HashMap<>();
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (null == ctx) {
            return;
        }

        try {
            super.channelActive(ctx);
            _channelGroup.add(ctx.channel());
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        }
    }






    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.info("收到客户端消息,msgClazz={},msgBody={}",
                msg.getClass().getSimpleName(),
                msg);

        if (msg instanceof GameMsgProtocol.UserEntryCmd) {
            GameMsgProtocol.UserEntryCmd cmd= (GameMsgProtocol.UserEntryCmd)msg;
            int userId = cmd.getUserId();
            String heroAvatar = cmd.getHeroAvatar();

            _userMap.put(userId, new User(userId, heroAvatar));

            GameMsgProtocol.UserEntryResult.Builder resultBuilder = GameMsgProtocol.UserEntryResult.newBuilder();
            resultBuilder.setUserId(userId);
            resultBuilder.setHeroAvatar(heroAvatar);

            // 构建结束并广播
            GameMsgProtocol.UserEntryResult newResult = resultBuilder.build();
            _channelGroup.writeAndFlush(newResult);

        }else  if (msg instanceof GameMsgProtocol.WhoElseIsHereCmd) {

            GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();

            for (User curUser : _userMap.values()) {
                if (null == curUser) {
                    continue;
                }
                GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder =
                        GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
                userInfoBuilder.setUserId(curUser.getUserId());
                userInfoBuilder.setHeroAvatar(curUser.getHeroAvatar());
                resultBuilder.addUserInfo(userInfoBuilder);

            }
            // 构建结束并广播
            GameMsgProtocol.WhoElseIsHereResult result = resultBuilder.build();
            ctx.writeAndFlush(result);

        }
    }
}
