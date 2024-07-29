package com.caspiantech.notification.ms.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

    @RabbitListener(queues = "${notification.queue.name}")
    public void receiveMessage(String message) {
        // Log the message
        logger.info("Received notification: {}", message);
    }

    @RabbitListener(queues = "${notification.dlq.name}")
    public void receiveDLQMessage(String message) {
        // Log the dead letter message
        logger.error("Received dead letter notification: {}", message);
    }
}