package com.teng.herostory.model;

/**
 * @program: nettyProject
 * @description:
 * @author: Mr.Teng
 * @create: 2021-01-01 15:02
 **/
public class User {
    //用户id
    private int userId;
    //英雄形象
    private String heroAvatar;
    //用户血量
    private int currHp;

    public int getCurrHp() {
        return currHp;
    }

    public void setCurrHp(int currHp) {
        this.currHp = currHp;
    }

    public MoveState getMoveState() {
        return moveState;
    }

    private final MoveState moveState = new MoveState();

    public User(int userId, String heroAvatar,int currHp) {
        this.userId = userId;
        this.heroAvatar = heroAvatar;
        this.currHp = currHp;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getHeroAvatar() {
        return heroAvatar;
    }

    public void setHeroAvatar(String heroAvatar) {
        this.heroAvatar = heroAvatar;
    }
}
