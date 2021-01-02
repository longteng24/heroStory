package com.teng.herostory.async;

import com.teng.herostory.MainThreadProcessor;
import sun.applet.Main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: nettyProject
 * @description: 异步操作服务器
 * @author: Mr.Teng
 * @create: 2021-01-02 16:50
 **/
public class AsyncOperationProcessor {

    /**
     * 单例对象
     */
    private static final AsyncOperationProcessor _instance = new AsyncOperationProcessor();

    /**
     * 创建单线程数组
     */
    private final ExecutorService[] _esArray = new ExecutorService[8];

    /**
     * 私有化构造
     */
    private AsyncOperationProcessor() {
        for (int i = 0; i < _esArray.length; i++) {
           final String threadName= "AsyncOperationProcessor[" + i + "]";
            _esArray[i]= Executors.newSingleThreadExecutor((r) -> {
                Thread thread = new Thread(r);
                thread.setName(threadName);
                return thread;
            });

        }
    }


    public static AsyncOperationProcessor getInstance() {
        return _instance;
    }

    /**
     * 执行异步操作
     * @param op
     */
    public void process(IAsyncOperation op) {
        if (null == op) {
            return;
        }

        int bindId =Math.abs(op.getBindId()) ;
        int esIndex = bindId % _esArray.length;

        _esArray[esIndex].submit(()->{
            //执行异步操作
            op.doAsync();
            //回到主线程 执行完成逻辑
            MainThreadProcessor.getInstance().process(op::doFinish);

        });
    }
}
