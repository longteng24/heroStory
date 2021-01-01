package com.teng.herostory;

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

    public User(int userId, String heroAvatar) {
        this.userId = userId;
        this.heroAvatar = heroAvatar;
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
