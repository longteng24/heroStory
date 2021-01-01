package com.teng.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.teng.herostory.msg.GameMsgProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: nettyProject
 * @description: 自定义解码器
 * @author: Mr.Teng
 * @create: 2021-01-01 13:23
 **/
public class GameMsgDecoder extends ChannelInboundHandlerAdapter {
    static  final Logger LOGGER = LoggerFactory.getLogger(GameMsgDecoder.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (null == ctx||
            null==msg) {
            return;
        }
        if (!(msg instanceof BinaryWebSocketFrame)) {
            return;
        }

        try {
            BinaryWebSocketFrame inputFrame = (BinaryWebSocketFrame) msg;
            ByteBuf byteBuf = inputFrame.content();

            // 粘包，黏包问题 netty已经处理   , 下面只是为了读消息头
            byteBuf.readShort();  // 读取消息长度
            int msgCode = byteBuf.readShort(); // 读取消息编号

            //拿到消息体
            byte[] msgBody = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(msgBody);


            // 获取消息构建器
            Message.Builder msgBuilder = GameMsgRecognizer.getBuilderByMsgCode(msgCode);
            msgBuilder.clear();
            msgBuilder.mergeFrom(msgBody);

            //构建消息实体
            Message cmd = msgBuilder.build();

            if (null != cmd) {
                // 处理完成，把处理好的消息放回流水线，继续执行
                ctx.fireChannelRead(cmd);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }

    }
}
