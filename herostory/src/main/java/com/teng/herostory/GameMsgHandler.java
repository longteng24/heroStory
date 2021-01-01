package com.teng.herostory;

import com.teng.herostory.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
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


    static final Map<Integer, User> _userMap = new HashMap<>();
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (null == ctx) {
            return;
        }

        try {
            super.channelActive(ctx);
            Broadcaster.addChannel(ctx.channel());
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    /**
     * 用户退出时，通知其他客户端
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx)  {
        try {
            super.handlerRemoved(ctx);

            Integer userId=(Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();

            if (userId == null) {
                return;
            }

            _userMap.remove(userId);

            GameMsgProtocol.UserQuitResult.Builder resultBuilder = GameMsgProtocol.UserQuitResult.newBuilder();
            resultBuilder.setQuitUserId(userId);

            GameMsgProtocol.UserQuitResult newResult = resultBuilder.build();
            Broadcaster.Broadcast(newResult);
        } catch (Exception ex) {
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

            //将用户id保存至session
            ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);

            GameMsgProtocol.UserEntryResult.Builder resultBuilder = GameMsgProtocol.UserEntryResult.newBuilder();
            resultBuilder.setUserId(userId);
            resultBuilder.setHeroAvatar(heroAvatar);

            // 构建结束并广播
            GameMsgProtocol.UserEntryResult newResult = resultBuilder.build();
            Broadcaster.Broadcast(newResult);

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

        } else if (msg instanceof GameMsgProtocol.UserMoveToCmd) {
           Integer userId=(Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
           if(null==userId){
               return;
           }
            // 用户移动
            GameMsgProtocol.UserMoveToCmd cmd=(GameMsgProtocol.UserMoveToCmd)msg;
            GameMsgProtocol.UserMoveToResult.Builder resultBuilder = GameMsgProtocol.UserMoveToResult.newBuilder();
            resultBuilder.setMoveUserId(userId);
            resultBuilder.setMoveToPosX(cmd.getMoveToPosX());
            resultBuilder.setMoveToPosY(cmd.getMoveToPosY());
            GameMsgProtocol.UserMoveToResult newResult = resultBuilder.build();
            Broadcaster.Broadcast(newResult);
        }
    }
}
