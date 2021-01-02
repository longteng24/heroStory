package com.teng.herostory;

import com.teng.herostory.cmdhandler.CmdHandlerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: nettyProject
 * @description: 服务端
 * @author: Mr.Teng
 * @create: 2021-01-01 10:27
 *  测试地址：  http://cdn0001.afrxvk.cn/hero_story/demo/step010/index.html?serverAddr=127.0.0.1:12345&userId=1
 * netty  Reactor模型
 * 服务器 与客户端通过 二进制来传递
 *    eg    传输数字13     按照字符串传 16字节，   按照数字传 8字节
 *
 *     消息协议 ： 前四字节（前两个表示消息长度（short int值表示），后两个表示消息编号（short int值表示） 表示消息的作用（消息类型编码，1.登录 2.退出）
 *     粘包：收发消息， 不知道读到哪里结束  (读消息头，消息长度，如果不够就暂存继续等)
 *
 *     protobuf协议  通过protobuf 协议文件 生成java代码
 *
 *   修改用户移动协议：  http://cdn0001.afrxvk.cn/hero_story/demo/step020/index.html?serverAddr=127.0.0.1:12345&userId=2
 *
 *   jps   jstack
 *
 *   用户登录  http://cdn0001.afrxvk.cn/hero_story/demo/step030/index.html?serverAddr=127.0.0.1:12345
 **/
public class ServerMain {
    static  final Logger LOGGER = LoggerFactory.getLogger(ServerMain.class);

    public static void main(String[] args) {
        PropertyConfigurator.configure(ServerMain.class.getClassLoader().getResourceAsStream("log4j.properties"));

        //绑定消息消息识别器
        GameMsgRecognizer.init();
        //消息处理器初始化
        CmdHandlerFactory.init();
        //初始化数据库连接
        MySqlSessionFactory.init();

        // 拉客的   accpet  客户端请求，建立连接，交给work处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //干活的      通过监听客户端读写事件 work来处理
        EventLoopGroup workerGroup = new NioEventLoopGroup();


        try {
            // 包装netty 操作界面
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            // 服务器监听的 channel
            b.channel(NioServerSocketChannel.class); // 服务器信道处理方式
            // 如果监听到客户端到达channel , 建立socketChannel
            b.childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 流式   预处理 解码等
                    ch.pipeline().addLast(
                            new HttpServerCodec(),
                            new HttpObjectAggregator(65535),                //消息长度限制
                            new WebSocketServerProtocolHandler("/websocket"),
                            new GameMsgDecoder(),  //自定义解码
                            new GameMsgEncoder(),  //自定义编码
                            new GameMsgHandler()   //自定义处理器
                    );

                }
            });

            b.option(ChannelOption.SO_BACKLOG, 128);
            b.childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(12345).sync();

            if (f.isSuccess()) {
                LOGGER.info("服务器成功启动");
            }

            f.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }




    }
}
