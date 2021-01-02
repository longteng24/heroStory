package com.teng.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.teng.herostory.cmdhandler.*;
import com.teng.herostory.model.UserManager;
import com.teng.herostory.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
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
        if (null == ctx) {
            return;
        }
        try {
            super.handlerRemoved(ctx);

            Integer userId=(Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();

            if (userId == null) {
                return;
            }
            //用户离线，移除相关数据
            UserManager.removeByUserId(userId);
            Broadcaster.removeChannel(ctx.channel());

            GameMsgProtocol.UserQuitResult.Builder resultBuilder = GameMsgProtocol.UserQuitResult.newBuilder();
            resultBuilder.setQuitUserId(userId);

            GameMsgProtocol.UserQuitResult newResult = resultBuilder.build();
            Broadcaster.broadcast(newResult);

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof GeneratedMessageV3) {
            MainThreadProcessor.getInstance().process(ctx,(GeneratedMessageV3)msg);
        }
    }

}
