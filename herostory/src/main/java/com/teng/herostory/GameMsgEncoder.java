package com.teng.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.teng.herostory.msg.GameMsgProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: nettyProject
 * @description: 消息的编码器
 * @author: Mr.Teng
 * @create: 2021-01-01 14:06
 **/
public class GameMsgEncoder extends ChannelOutboundHandlerAdapter {
    static final Logger LOGGER = LoggerFactory.getLogger(GameMsgEncoder.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (null == ctx||null==msg) {
            return;
        }
        try {
            // 判断消息类型，
            if (!(msg instanceof GeneratedMessageV3)) {
                super.write(ctx, msg, promise);
                return;
            }
            //消息编码
            int msgCode=-1;
            //消息体
            byte[] msgBody;
            if (msg instanceof GameMsgProtocol.UserEntryResult) {
                msgCode = GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE;
                msgBody= ((GameMsgProtocol.UserEntryResult) msg).toByteArray();

            }else  if (msg instanceof GameMsgProtocol.WhoElseIsHereResult) {
                msgCode = GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE;
                msgBody= ((GameMsgProtocol.WhoElseIsHereResult) msg).toByteArray();

            }else {
                LOGGER.error("无法识别的消息类型，msgClazz={}",
                        msg.getClass().getSimpleName());
                super.write(ctx, msg, promise);
                return;
            }


            ByteBuf byteBuf = ctx.alloc().buffer();
            byteBuf.writeShort((short) msgBody.length);  //长度
            byteBuf.writeShort((short) msgCode);         //编码
            byteBuf.writeBytes(msgBody);                 //
            BinaryWebSocketFrame outputFrame = new BinaryWebSocketFrame(byteBuf);
            super.write(ctx, outputFrame, promise);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }

    }
}
