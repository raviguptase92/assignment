package com.insider.assignment.messaging.listener;

import com.insider.assignment.service.AsyncStoryUpdateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopStoryUpdateListener {

    @Autowired
    private AsyncStoryUpdateService asyncStoryUpdateService;

    private static final Logger logger = LogManager.getLogger(TopStoryUpdateListener.class);

    @RabbitListener(queues = "newTopStoryQueue")
    public void onMessage(String message) throws Exception {
        logger.info("Update message received {}", message);
        asyncStoryUpdateService.updateTopStories();
        logger.info("Update event success");
    }
}
