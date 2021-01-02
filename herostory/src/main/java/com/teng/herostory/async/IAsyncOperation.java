package com.teng.herostory.async;

/**
 * @program: nettyProject
 * @description:
 * @author: Mr.Teng
 * @create: 2021-01-02 17:17
 **/
public interface IAsyncOperation {

    /**
     * 获取绑定id
     *
     * @return
     */
    default int getBindId() {
        return 0;
    }
    /**
     * 执行异步操作
     */
    void doAsync();

    /**
     * 执行完成逻辑
     */
    default void doFinish() {

    }
}
