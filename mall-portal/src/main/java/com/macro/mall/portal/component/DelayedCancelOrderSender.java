package com.macro.mall.portal.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DelayedCancelOrderSender {
    private static Logger LOGGER = LoggerFactory.getLogger(DelayedCancelOrderSender.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${spring.kafka.order-topic}")
    private String kafkaOrderTopic;

    public void sendDelayedMessage(Long orderId, final long delayTimes) {
        // 创建一个新线程来发送延迟消息
        Thread delayedThread = new Thread(() -> {
            try {
                LOGGER.info("Liminghui sendMessage:"+ orderId);
                // 等待指定时间后再发送消息
                Thread.sleep(delayTimes); // 这里设置为 n秒钟作为延迟时间

                // 获取KafkaTemplate对象并发送消息
                KafkaTemplate<String, Long> kafkaTemplate = applicationContext.getBean(KafkaTemplate.class);
                kafkaTemplate.send(kafkaOrderTopic, orderId);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 启动线程开始发送延迟消息
        delayedThread.start();
    }
}