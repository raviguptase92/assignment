package com.insider.assignment.config;

import com.insider.assignment.constant.Constants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class StoryRefreshIntervalTest {

    @InjectMocks
    private StoryRefreshInterval storyRefreshInterval = new StoryRefreshInterval();

    @Mock
    private CacheManager cacheManager;

    public static final String STORY_REFRESH_CACHE = "StoryRefreshInterval";

    public static final String LAST_REFRESH_TIME = "lastRefreshTime";

    public static final int storyCacheTime = 10;

    @Before
    public void setUp() {
        cacheManager = Mockito.mock(ConcurrentMapCacheManager.class);
        ReflectionTestUtils.setField(storyRefreshInterval, "cacheManager", cacheManager);
        ReflectionTestUtils.setField(storyRefreshInterval, "storyCacheTime", storyCacheTime);
    }

    @Test
    public void testNeedUpdateWhenCacheNotPresent() {
        when(cacheManager.getCache(STORY_REFRESH_CACHE)).thenReturn(null);
        boolean value = storyRefreshInterval.needUpdate();
        assertTrue(value);
    }

    @Test
    public void testNeedUpdateWhenCacheIsEmpty() {
        Cache cache = new ConcurrentMapCache(STORY_REFRESH_CACHE);
        when(cacheManager.getCache(STORY_REFRESH_CACHE)).thenReturn(cache);
        boolean value = storyRefreshInterval.needUpdate();
        assertTrue(value);
    }

    @Test
    public void testNeedUpdateWhenLastRefreshTimeBeforeStoryCacheTime() {
        Cache cache = new ConcurrentMapCache(STORY_REFRESH_CACHE);
        Date pastTime = new Date(System.currentTimeMillis() - ((storyCacheTime+1) * Constants.ONE_MINUTE_IN_MILLIS));
        cache.put(LAST_REFRESH_TIME, pastTime);

        when(cacheManager.getCache(STORY_REFRESH_CACHE)).thenReturn(cache);
        boolean value = storyRefreshInterval.needUpdate();
        assertTrue(value);
    }

    @Test
    public void testNeedUpdateWhenLastRefreshTimeAfterStoryCacheTime() {
        Cache cache = new ConcurrentMapCache(STORY_REFRESH_CACHE);
        Date pastTime = new Date(System.currentTimeMillis() - ((storyCacheTime-1) * Constants.ONE_MINUTE_IN_MILLIS));
        cache.put(LAST_REFRESH_TIME, pastTime);

        when(cacheManager.getCache(STORY_REFRESH_CACHE)).thenReturn(cache);
        boolean value = storyRefreshInterval.needUpdate();
        assertFalse(value);
    }

    @Test
    public void testMarkRefreshCompleteHappyFlow() {
        Cache cache = new ConcurrentMapCache(STORY_REFRESH_CACHE);
        when(cacheManager.getCache(STORY_REFRESH_CACHE)).thenReturn(cache);

        storyRefreshInterval.markRefreshComplete(true);
        assertNotNull(cache.get(LAST_REFRESH_TIME));
    }

    @Test
    public void testMarkRefreshCompleteWhenCacheIsNull() {
        storyRefreshInterval.markRefreshComplete(true);
    }
}
