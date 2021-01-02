package com.teng.herostory;


import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.teng.herostory.msg.GameMsgProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: nettyProject
 * @description: 消息识别器
 * @author: Mr.Teng
 * @create: 2021-01-01 21:43
 **/
public final class GameMsgRecognizer {
    static  final Logger LOGGER = LoggerFactory.getLogger(GameMsgDecoder.class);
    private GameMsgRecognizer() {
    }

    /**
     * 消息编码->消息对象字典
     */
    private static Map<Integer,  GeneratedMessageV3> _msgCodeAndMsgObjMap = new HashMap<>();

    /**
     * 消息类->消息编号
     */
    private static Map<Class<?>,Integer> _msgClazzAndMsgCodeMap = new HashMap<>();


   public static void init() {
           LOGGER.info("==完成消息类与消息编号映射====");
        //获取该类的内部类
        Class<?>[] innerClazzArray = GameMsgProtocol.class.getDeclaredClasses();

        for (Class<?> innerClazz : innerClazzArray) {
            //该类不为空，并要是v3的子类
            if (innerClazz == null ||
                    !GeneratedMessageV3.class.isAssignableFrom(innerClazz)) {

                continue;
            }
            String clazzName = innerClazz.getSimpleName().toLowerCase();

            for (GameMsgProtocol.MsgCode msgCode : GameMsgProtocol.MsgCode.values()) {
              if(msgCode==null) {continue;}

                String msgCodeName = msgCode.name()
                        .replaceAll("_","")
                        .toLowerCase();

                if (!msgCodeName.startsWith(clazzName)) {
                    continue;
                }

                try {
                 Object returnObj=   innerClazz.getDeclaredMethod("getDefaultInstance").invoke(innerClazz);
                 LOGGER.info("{} <==>{}" ,
                         innerClazz.getName(),
                         msgCode.getNumber()
                 );
                    _msgCodeAndMsgObjMap.put(
                            msgCode.getNumber(),
                            (GeneratedMessageV3) returnObj
                    );


                    _msgClazzAndMsgCodeMap.put(
                            innerClazz,
                            msgCode.getNumber()
                    );

                } catch (Exception ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }

            }

        }

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
        if (msgClass == null || _msgClazzAndMsgCodeMap.get(msgClass )== null) {
            return -1;
        }
        return _msgClazzAndMsgCodeMap.get(msgClass).intValue();
    }
}
