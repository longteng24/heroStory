package com.teng.herostory.async;

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
     * 创建一个单线程的线程池
     */
    private final ExecutorService _es = Executors.newSingleThreadExecutor((newRunable) -> {
        Thread newThread = new Thread(newRunable);
        newThread.setName("AsyncOperationProcessor");
        return newThread;
    });

    /**
     * 私有化构造
     */
    private AsyncOperationProcessor() {
    }


    public static AsyncOperationProcessor getInstance() {
        return _instance;
    }

    /**
     * 执行异步操作
     * @param runnable
     */
    public void process(Runnable runnable) {
        if (null == runnable) {
            return;
        }
        _es.submit(runnable);
    }
}
