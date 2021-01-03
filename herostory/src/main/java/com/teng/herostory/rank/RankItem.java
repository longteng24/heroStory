package com.teng.herostory.rank;

/**
 * @program: nettyProject
 * @description: 排名条目
 * @author: Mr.Teng
 * @create: 2021-01-03 09:48
 **/
public class RankItem {

    /**
     * 排名id
     */
    private int rankId;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 英雄形象
     */
    private String heroAvatar;

    /**
     * 胜利场次
     */
    public int win;

    public int getRankId() {
        return rankId;
    }

    public void setRankId(int rankId) {
        this.rankId = rankId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeroAvatar() {
        return heroAvatar;
    }

    public void setHeroAvatar(String heroAvatar) {
        this.heroAvatar = heroAvatar;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }
}
