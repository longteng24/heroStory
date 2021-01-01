package com.teng.herostory.cmdhandler;

import com.google.protobuf.GeneratedMessageV3;
import com.teng.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: nettyProject
 * @description:
 * @author: Mr.Teng
 * @create: 2021-01-01 20:56
 **/
public final class CmdHandlerFactory {

    private static Map<Class<?>, ICmdHandler<? extends GeneratedMessageV3>> _handleMap = new HashMap<>();

    private CmdHandlerFactory() {

    }

     static {
        _handleMap.put(GameMsgProtocol.UserEntryCmd.class, new UserEntryCmdHandler());
        _handleMap.put(GameMsgProtocol.WhoElseIsHereCmd.class, new WhoElseIsHereCmdHandler());
        _handleMap.put(GameMsgProtocol.UserMoveToCmd.class, new UserMoveToHandler());
    }

    public static ICmdHandler<? extends GeneratedMessageV3> create(Class<?> msgClass) {
        if (null == msgClass) {
            return null;
        }

        return _handleMap.get(msgClass);
    }
}
