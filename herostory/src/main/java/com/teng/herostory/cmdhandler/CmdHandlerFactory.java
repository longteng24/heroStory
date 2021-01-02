package com.teng.herostory.cmdhandler;

import com.google.protobuf.GeneratedMessageV3;
import com.teng.herostory.GameMsgDecoder;
import com.teng.herostory.util.PackageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @program: nettyProject
 * @description:
 * @author: Mr.Teng
 * @create: 2021-01-01 20:56
 **/
public final class CmdHandlerFactory {
    static  final Logger LOGGER = LoggerFactory.getLogger(GameMsgDecoder.class);
    private static Map<Class<?>, ICmdHandler<? extends GeneratedMessageV3>> _handleMap = new HashMap<>();

    private CmdHandlerFactory() {

    }

    public static void init() {
        LOGGER.info("完成命令处理器的关联");
        final String packageName = CmdHandlerFactory.class.getPackage().getName();
        // 获取指定包名下，所有ICmdHandler 类型的类
        Set<Class<?>> classSet = PackageUtil.listSubClazz(
                packageName,
                true,
                ICmdHandler.class);


        for (Class<?> handlerClass : classSet) {
            //如果是抽象类则不处理
            if (null == handlerClass ||
                    0 != (handlerClass.getModifiers() & Modifier.ABSTRACT)) {
                continue;
            }

            Method[] methodArray = handlerClass.getDeclaredMethods();
            //消息类型
            Class<?> msgClazz=null;
             //通过验证handler方法参数类型，来过滤， 并获取第二个参数类型名称
            for (Method curMethod : methodArray) {
                if (null == curMethod ||
                        !curMethod.getName().equals("handle")) {
                    continue;
                }
                 //获取参数类型
                Class<?>[] paramTypeArray = curMethod.getParameterTypes();

                //至少有两个参数， 不是v3,必须是v3的子类
                if (paramTypeArray.length < 2 ||
                        paramTypeArray[1] == GeneratedMessageV3.class ||
                        !GeneratedMessageV3.class.isAssignableFrom(paramTypeArray[1])) {
                    continue;
                }

                msgClazz = paramTypeArray[1];
                break;
            }
            if (msgClazz==null) {
                continue;
            }

            try {
                ICmdHandler<?> newHandler = (ICmdHandler) handlerClass.newInstance();
                LOGGER.info("{}==={}",msgClazz.getName(),handlerClass.getName());
                _handleMap.put(msgClazz, newHandler);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static ICmdHandler<? extends GeneratedMessageV3> create(Class<?> msgClass) {
        if (null == msgClass) {
            return null;
        }

        return _handleMap.get(msgClass);
    }
}
