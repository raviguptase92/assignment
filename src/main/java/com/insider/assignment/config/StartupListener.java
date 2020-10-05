package com.insider.assignment.config;

import com.insider.assignment.service.AsyncStoryUpdateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private AsyncStoryUpdateService asyncStoryUpdateService;

    private static final Logger logger = LogManager.getLogger(StartupListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.info("Startup event received !!");
        try {
            asyncStoryUpdateService.updateTopStories();
        } catch (Exception ex) {
            logger.error("System startup error", ex);
        }
        logger.info("Startup completed !!");
    }
}
