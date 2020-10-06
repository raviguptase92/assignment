package com.insider.assignment.service.impl;

import com.insider.assignment.config.StoryRefreshInterval;
import com.insider.assignment.constant.Constants;
import com.insider.assignment.dto.hn.HNItem;
import com.insider.assignment.entity.TopStory;
import com.insider.assignment.repository.StoryRepository;
import com.insider.assignment.repository.TopStoryRepository;
import com.insider.assignment.service.HttpService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class AsyncStoryUpdateServiceImplTest {

    @InjectMocks
    private AsyncStoryUpdateServiceImpl asyncStoryUpdateService = new AsyncStoryUpdateServiceImpl();

    @Mock
    private HttpService httpService;

    @Mock
    private TopStoryRepository topStoryRepository;

    @Mock
    private StoryRepository storyRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private StoryRefreshInterval storyRefreshInterval;

    private final int storyCacheTime = 10;

    private final int storyUpdateRetryTime = 20;

    @Before
    public void setUp() {
        httpService = Mockito.mock(HttpService.class);
        topStoryRepository = Mockito.mock(TopStoryRepository.class);
        storyRepository = Mockito.mock(StoryRepository.class);
        rabbitTemplate = Mockito.mock(RabbitTemplate.class);
        cacheManager = Mockito.mock(CacheManager.class);
        storyRefreshInterval = Mockito.mock(StoryRefreshInterval.class);
        ReflectionTestUtils.setField(asyncStoryUpdateService, "httpService", httpService);
        ReflectionTestUtils.setField(asyncStoryUpdateService, "topStoryRepository", topStoryRepository);
        ReflectionTestUtils.setField(asyncStoryUpdateService, "storyRepository", storyRepository);
        ReflectionTestUtils.setField(asyncStoryUpdateService, "rabbitTemplate", rabbitTemplate);
        ReflectionTestUtils.setField(asyncStoryUpdateService, "cacheManager", cacheManager);
        ReflectionTestUtils.setField(asyncStoryUpdateService, "storyRefreshInterval", storyRefreshInterval);
        ReflectionTestUtils.setField(asyncStoryUpdateService, "storyCacheTime", storyCacheTime);
        ReflectionTestUtils.setField(asyncStoryUpdateService, "storyUpdateRetryTime", storyUpdateRetryTime);
        ReflectionTestUtils.setField(asyncStoryUpdateService, "executorService", Executors.newFixedThreadPool(Constants.EXECUTOR_THREAD_COUNT));
    }

    @Test
    public void testRaiseUpdateEventWithException() {
        doThrow(new RuntimeException("Any Exception")).when(rabbitTemplate).convertAndSend(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any(MessagePostProcessor.class));
        asyncStoryUpdateService.raiseUpdateEvent(storyCacheTime*60000);
    }

    @Test
    public void testRaiseUpdateEventWithoutException() {
        asyncStoryUpdateService.raiseUpdateEvent(storyCacheTime*60000);
    }

    @Test
    public void testUpdateTopStoriesWhenUpdateNotRequired() {
        when(storyRefreshInterval.needUpdate()).thenReturn(false);
        asyncStoryUpdateService.updateTopStories();
    }

    @Test
    public void testUpdateTopStoriesWhenHNListEmpty() {
        when(storyRefreshInterval.needUpdate()).thenReturn(true);
        when(httpService.makeGetRequest(Mockito.anyString(), Mockito.any())).thenReturn(new ArrayList<>());
        asyncStoryUpdateService.updateTopStories();
    }

    @Test
    public void testUpdateTopStoriesWhenHNListNonEmpty() {
        when(storyRefreshInterval.needUpdate()).thenReturn(true);

        List<Integer> storyIds = new ArrayList<>();
        storyIds.add(123);
        storyIds.add(321);

        HNItem story1 = new HNItem();
        story1.setId(123L);
        story1.setScore(100);
        HNItem story2 = new HNItem();
        story2.setId(321L);
        story2.setScore(200);
        when(httpService.makeGetRequest(Constants.TOP_STORY_API, List.class)).thenReturn(storyIds);
        when(httpService.makeGetRequest(String.format(Constants.ITEM_API, 123), HNItem.class)).thenReturn(story1);
        when(httpService.makeGetRequest(String.format(Constants.ITEM_API, 321), HNItem.class)).thenReturn(story2);
        asyncStoryUpdateService.updateTopStories();
    }

    @Test
    public void testUpdateTopStoriesWhenHNListNonEmptyAndExistingStoriesPresent() {
        when(storyRefreshInterval.needUpdate()).thenReturn(true);

        List<Integer> storyIds = new ArrayList<>();
        storyIds.add(123);
        storyIds.add(321);

        HNItem story1 = new HNItem();
        story1.setId(123L);
        story1.setScore(100);
        HNItem story2 = new HNItem();
        story2.setId(321L);
        story2.setScore(200);

        List<TopStory> existingTopStories = new ArrayList<>();
        TopStory topStory = new TopStory();
        topStory.setId(121L);
        topStory.setScore(101);
        existingTopStories.add(topStory);
        when(httpService.makeGetRequest(Constants.TOP_STORY_API, List.class)).thenReturn(storyIds);
        when(httpService.makeGetRequest(String.format(Constants.ITEM_API, 123), HNItem.class)).thenReturn(story1);
        when(httpService.makeGetRequest(String.format(Constants.ITEM_API, 321), HNItem.class)).thenReturn(story2);
        when(topStoryRepository.findAll()).thenReturn(existingTopStories);
        when(cacheManager.getCache(Constants.TOP_STORY_CACHE)).thenReturn(new ConcurrentMapCache(Constants.TOP_STORY_CACHE));
        when(cacheManager.getCache(Constants.PAST_STORY_CACHE)).thenReturn(new ConcurrentMapCache(Constants.PAST_STORY_CACHE));
        asyncStoryUpdateService.updateTopStories();
    }
}
