package com.insider.assignment.service.impl;

import com.insider.assignment.config.StoryRefreshInterval;
import com.insider.assignment.constant.Constants;
import com.insider.assignment.dto.hn.HNItem;
import com.insider.assignment.dto.StoryDTO;
import com.insider.assignment.entity.Story;
import com.insider.assignment.entity.TopStory;
import com.insider.assignment.repository.StoryRepository;
import com.insider.assignment.repository.TopStoryRepository;
import com.insider.assignment.service.AsyncStoryUpdateService;
import com.insider.assignment.service.HttpService;
import com.insider.assignment.utils.Converter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Transactional
public class AsyncStoryUpdateServiceImpl implements AsyncStoryUpdateService {

    @Value("${story.cache.eviction.time.minutes:10}")
    private int storyCacheTime;

    @Value("${story.update.retry.time.seconds:20}")
    private int storyUpdateRetryTime;

    @Autowired
    private HttpService httpService;

    @Autowired
    private TopStoryRepository topStoryRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private StoryRefreshInterval storyRefreshInterval;

    private ExecutorService executorService;

    private static final Logger logger = LogManager.getLogger(AsyncStoryUpdateServiceImpl.class);

    @PostConstruct
    void init() {
        executorService = Executors.newFixedThreadPool(Constants.EXECUTOR_THREAD_COUNT);
    }

    @Override
    public void raiseUpdateEvent(long delay) {
        try {
            rabbitTemplate.convertAndSend("delayed-update-exchange", "all", "Update Event ( " + System.currentTimeMillis() + " )", message -> {
                message.getMessageProperties().setHeader("x-delay", delay);
                return message;
            });
        } catch (Exception ex) {
            logger.error("Error occurred while sending update event to queue : ", ex);
        }
    }

    @Override
    public void updateTopStories() {
        if(!storyRefreshInterval.needUpdate()) {
            logger.info("Update not required");
            return;
        }

        try {
            logger.debug("Update story started");
            List<Integer> topStories = (List<Integer>) httpService.makeGetRequest(Constants.TOP_STORY_API, List.class);
            List<StoryDTO> storyDTOS = new ArrayList<>();
            List<Future<HNItem>> hnItemList = new ArrayList<>();
            for (Integer topStory : topStories) {
                Future<HNItem> hnItemFuture = executorService.submit(() -> {
                    String storyUrl = String.format(Constants.ITEM_API, topStory);
                    return httpService.makeGetRequest(storyUrl, HNItem.class);
                });
                hnItemList.add(hnItemFuture);
            }

            for (Future<HNItem> hnItemFuture: hnItemList) {
                storyDTOS.add(Converter.buildStoryDTOFromHNStory(hnItemFuture.get()));
            }

            Collections.sort(storyDTOS);
            List<TopStory> top10Stories = new ArrayList<>();
            for (int i=0 ; i<10 && i<storyDTOS.size() ; i++) {
                top10Stories.add(Converter.buildTopStoryFromDTO(storyDTOS.get(i)));
            }

            List<TopStory> existingTopStories = topStoryRepository.findAll();
            if(!CollectionUtils.isEmpty(existingTopStories)) {
                logger.debug("Existing stories size : {}", existingTopStories.size());
                List<Story> pastStories = new ArrayList<>();
                for (TopStory existingTopStory : existingTopStories) {
                    pastStories.add(Converter.buildStoryFromTopStory(existingTopStory));
                }
                logger.debug("Past stories size : {}", pastStories.size());

                // Save current top stories to past stories
                storyRepository.saveAll(pastStories);

                // delete existing top stories
                topStoryRepository.deleteAll();
            }

            // Save new top 10 stories
            topStoryRepository.saveAll(top10Stories);

            // Evict top story cache
            evictStoryCaches();
            storyRefreshInterval.markRefreshComplete(true);
            logger.debug("Stories are updated successfully");

            // Raise delayed event for next update : Configurable delay
            raiseUpdateEvent(storyCacheTime * Constants.ONE_MINUTE_IN_MILLIS);
        } catch (Exception ex) {
            logger.error("Error occurred while updating top stories, Exception : ", ex);

            // will retry after retry duration
            raiseUpdateEvent(storyUpdateRetryTime * 1000);
            logger.error("Will retry after 20 seconds");
        }
    }

    private void evictStoryCaches() {
        // Evict top story cache
        Cache cache = cacheManager.getCache(Constants.TOP_STORY_CACHE);
        if(cache != null) {
            logger.info("{} cache not present", Constants.TOP_STORY_CACHE);
            cache.clear();
        }

        // Evict past story cache
        Cache pastStoryCache = cacheManager.getCache(Constants.PAST_STORY_CACHE);
        if(pastStoryCache != null) {
            logger.info("{} cache not present", Constants.PAST_STORY_CACHE);
            pastStoryCache.clear();
        }
        logger.debug("Caches evicted successfully");
    }
}
