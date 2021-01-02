package com.teng.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.teng.herostory.cmdhandler.CmdHandlerFactory;
import com.teng.herostory.cmdhandler.ICmdHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @program: nettyProject
 * @description: 主线程处理器
 * @author: Mr.Teng
 * @create: 2021-01-02 14:32
 **/
public class MainThreadProcessor {
    /**
     * 日志对象
     */
    static  final Logger LOGGER = LoggerFactory.getLogger(MainThreadProcessor.class);

    /**
     * 单例对象
     */
    private static final MainThreadProcessor _instance = new MainThreadProcessor();

    /**
     * 创建一个单线程
     */
    private final ExecutorService _es = Executors.newSingleThreadExecutor((r) -> {
        Thread newThread = new Thread(r);
        newThread.setName("MainThreadProcessor");
        return newThread;
    });

    /**
     * 私有化默认构造器
     */
    private MainThreadProcessor() {
    }

    /**
     * 获取单例对象
     * @return 主流程处理器
     */
    public static MainThreadProcessor getInstance() {
        return _instance;
    }

    /**
     * 处理消息
     * @param ctx 客户端信道上下文
     * @param msg 消息对象
     */
    public void process(ChannelHandlerContext ctx, GeneratedMessageV3 msg) {
        if (ctx == null || msg == null) {
            return;
        }
        LOGGER.info("收到客户端消息,msgClazz={},msgBody={}",
                msg.getClass().getSimpleName(),
                msg);

        this._es.submit(()->{

                ICmdHandler<? extends GeneratedMessageV3> cmdHandler = CmdHandlerFactory.create(msg.getClass());
                if (cmdHandler ==null) {
                    LOGGER.error("未找到对应的指令处理器,msgClazz={}",
                            msg.getClass().getName()
                    );
                             return;
                }
            try{
                cmdHandler.handle(ctx,cast(msg));
            }catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        });

    }

    /**
     * 处理runable实例
     * @param r
     */
    public void process(Runnable r) {
        if (null == r) {
            return;
        }
        _es.submit(r);
    }

    /**
     * 转型
     * @param msg
     * @param <TCmd>
     * @return
     */
    private static <TCmd extends GeneratedMessageV3> TCmd cast(Object msg) {
        if (null == msg) {
            return null;
        }
        return (TCmd)msg;
    }
}
