package com.caspiantech.notification.ms.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private final Logger logger = LoggerFactory.getLogger(NotificationListener.class);

    @RabbitListener(queues = "${notification.queue.name}")
    public void receiveMessage(String message) {
        // Log the message
        logger.info("Received notification: {}", message);
    }
}