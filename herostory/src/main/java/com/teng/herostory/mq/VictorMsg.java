package com.teng.herostory.mq;

/**
 * @program: nettyProject
 * @description: 胜利者消息
 * @author: Mr.Teng
 * @create: 2021-01-03 10:47
 **/
public class VictorMsg {
    /**
     * 胜利者Id
     */
    private int winnerId;
    /**
     * 失败者id
     */
    private int loserId;

    public int getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    public int getLoserId() {
        return loserId;
    }

    public void setLoserId(int loserId) {
        this.loserId = loserId;
    }

    public VictorMsg(int winnerId, int loserId) {
        this.winnerId = winnerId;
        this.loserId = loserId;
    }
}
