package com.teng.herostory.mq;

import com.alibaba.fastjson.JSONObject;
import com.teng.herostory.GameMsgDecoder;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: nettyProject
 * @description: 消息队列生产者
 * @author: Mr.Teng
 * @create: 2021-01-03 10:35
 **/
public class MqProducer {
    static  final Logger LOGGER = LoggerFactory.getLogger(GameMsgDecoder.class);

    /**
     * 消息队列生产者
     */
    private static DefaultMQProducer _producer = null;

    /**
     * 私有化构造器
     */
    private MqProducer() {
    }

    /**
     * 初始化
     */
    public static void init() {
        try {
            DefaultMQProducer producer = new DefaultMQProducer("herostory");
            producer.setNamesrvAddr("121.36.28.218:9876");
            producer.start();
            //失败重试次数
            producer.setRetryTimesWhenSendAsyncFailed(3);

            _producer = producer;

            LOGGER.info("消息队列(生产者) 连接成功！");
        }catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 发送消息
     * @param topic 主题
     * @param msg   消息
     */
    public static void sendMsg(String topic, Object msg) {
        if (null == topic ||
                null == msg) {
            return;
        }

        Message newMsg = new Message();
        newMsg.setTopic(topic);
        newMsg.setBody(JSONObject.toJSONBytes(msg));

        try {
            //发送消息
            _producer.send(newMsg);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
