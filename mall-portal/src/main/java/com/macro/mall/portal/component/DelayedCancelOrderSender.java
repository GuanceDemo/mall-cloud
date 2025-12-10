package com.macro.mall.portal.component;

import com.macro.mall.portal.util.ConstanceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class DelayedCancelOrderSender {
    private static Logger logger = LoggerFactory.getLogger(DelayedCancelOrderSender.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${spring.kafka.order-topic}")
    private String kafkaOrderTopic;

    public void sendDelayedMessage(Long orderId, final long delayTimes) {
        ConstanceUtils.executor.submit(()->{
            try {
                logger.info("send message to kafka start - sendMessage:{},delayTimes:{}", orderId,delayTimes);
                // 等待指定时间后再发送消息
                Thread.sleep(delayTimes); // 这里设置为 n秒钟作为延迟时间

                // 获取KafkaTemplate对象并发送消息
                kafkaTemplate.send(kafkaOrderTopic, orderId);
                logger.info("send message to kafka success - sendMessage:{},delayTimes:{}", orderId,delayTimes);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}