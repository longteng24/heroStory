package com.teng.herostory;


import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.teng.herostory.cmdhandler.ICmdHandler;
import com.teng.herostory.cmdhandler.UserEntryCmdHandler;
import com.teng.herostory.cmdhandler.UserMoveToHandler;
import com.teng.herostory.cmdhandler.WhoElseIsHereCmdHandler;
import com.teng.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: nettyProject
 * @description: 消息识别器
 * @author: Mr.Teng
 * @create: 2021-01-01 21:43
 **/
public final class GameMsgRecognizer {
    private GameMsgRecognizer() {
    }

    /**
     * 消息编码->消息对象字典
     */
    private static Map<Integer,  GeneratedMessageV3> _msgCodeAndMsgObjMap = new HashMap<>();

    /**
     * 消息类->消息编号
     */
    private static Map<Class<?>,Integer> _clazzAndMsgCodeMap = new HashMap<>();


    static {
        _msgCodeAndMsgObjMap.put(GameMsgProtocol
                .MsgCode.USER_ENTRY_CMD_VALUE, GameMsgProtocol.UserEntryCmd.getDefaultInstance());
        _msgCodeAndMsgObjMap.put(GameMsgProtocol
                .MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE, GameMsgProtocol.WhoElseIsHereCmd.getDefaultInstance());
        _msgCodeAndMsgObjMap.put(GameMsgProtocol
                .MsgCode.USER_MOVE_TO_CMD_VALUE, GameMsgProtocol.UserMoveToCmd.getDefaultInstance());

        _clazzAndMsgCodeMap.put(GameMsgProtocol.UserEntryResult.class, GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE);
        _clazzAndMsgCodeMap.put(GameMsgProtocol.WhoElseIsHereResult.class, GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE);
        _clazzAndMsgCodeMap.put(GameMsgProtocol.UserMoveToResult.class, GameMsgProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE);
        _clazzAndMsgCodeMap.put(GameMsgProtocol.UserQuitResult.class, GameMsgProtocol.MsgCode.USER_QUIT_RESULT_VALUE);

    }
    public static Message.Builder getBuilderByMsgCode(int msgCode) {
        if (msgCode < 0) {
            return null;
        }
        GeneratedMessageV3 defaultMsg = _msgCodeAndMsgObjMap.get(msgCode);
        if (defaultMsg == null) {
            return null;
        }
        return defaultMsg.newBuilderForType();

    }

    /**
     * 根据消息类，获取消息编号
     * @param msgClass
     * @return
     */
    public static int getMsgCodeByClazz(Class<?> msgClass) {
        if (msgClass == null || _clazzAndMsgCodeMap.get(msgClass )== null) {
            return -1;
        }
        return _clazzAndMsgCodeMap.get(msgClass).intValue();
    }
}
