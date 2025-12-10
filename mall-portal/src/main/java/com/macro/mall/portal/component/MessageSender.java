package com.macro.mall.portal.component;

import com.macro.mall.portal.util.ConstanceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {
    private static Logger logger = LoggerFactory.getLogger(MessageSender.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Value("${spring.kafka.message-topic}")
    private String kafkaOrderTopic;

    public void sendMessage(String message){
        ConstanceUtils.executor.submit(()->{
            kafkaTemplate.send(kafkaOrderTopic, message);
        });
    }
}
