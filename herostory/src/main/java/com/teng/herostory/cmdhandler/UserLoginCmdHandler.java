package com.teng.herostory.cmdhandler;

import com.teng.herostory.login.db.LoginService;
import com.teng.herostory.login.db.UserEntity;
import com.teng.herostory.model.User;
import com.teng.herostory.model.UserManager;
import com.teng.herostory.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * @program: nettyProject
 * @description: 用户登录处理
 * @author: Mr.Teng
 * @create: 2021-01-02 16:07
 **/
public class UserLoginCmdHandler implements ICmdHandler<GameMsgProtocol.UserLoginCmd> {

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserLoginCmd cmd) {
        if (null == ctx ||
                null == cmd) {
            return;
        }

        String userName = cmd.getUserName();
        String password = cmd.getPassword();

        if (null == userName ||
                null == password) {
            return;
        }

        //获取用户实体
        UserEntity userEntity = LoginService.getInstance().userLogin(userName, password);

        GameMsgProtocol.UserLoginResult.Builder resultBuilder = GameMsgProtocol.UserLoginResult.newBuilder();

        if (null == userEntity) {
            resultBuilder.setUserId(-1);
            resultBuilder.setUserName("");
            resultBuilder.setHeroAvatar("");
        } else {




            UserManager.addUser(new User(userEntity.getUserId(), userEntity.getUserName(),userEntity.getHeroAvatar(),100));

            //将用户id保存至session
            ctx.channel().attr(AttributeKey.valueOf("userId")).set(userEntity.getUserId());

            resultBuilder.setUserId(userEntity.getUserId());
            resultBuilder.setUserName(userEntity.getUserName());
            resultBuilder.setHeroAvatar(userEntity.getHeroAvatar());
        }

        GameMsgProtocol.UserLoginResult newResult = resultBuilder.build();
        ctx.writeAndFlush(newResult);


    }
}
