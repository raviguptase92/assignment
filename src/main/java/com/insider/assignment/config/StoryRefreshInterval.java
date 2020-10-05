package com.insider.assignment.config;

import com.insider.assignment.constant.Constants;
import com.insider.assignment.service.impl.CommentServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class StoryRefreshInterval {

    @Value("${story.cache.eviction.time.minutes:10}")
    private int storyCacheTime;

    @Autowired
    private CacheManager cacheManager;

    public static final String STORY_REFRESH_CACHE = "StoryRefreshInterval";

    public static final String LAST_REFRESH_TIME = "lastRefreshTime";

    private static final Logger logger = LogManager.getLogger(CommentServiceImpl.class);

    public boolean needUpdate() {
        Cache timeCache = cacheManager.getCache(STORY_REFRESH_CACHE);
        if(timeCache == null) {
            return true;
        }

        Date lastRefreshTime = timeCache.get(LAST_REFRESH_TIME, Date.class);
        if(lastRefreshTime == null) {
            logger.debug("{} is null", LAST_REFRESH_TIME);
            return true;
        }

        Date currentTime = new Date();
        Date relativeTime = new Date(lastRefreshTime.getTime() + (storyCacheTime * Constants.ONE_MINUTE_IN_MILLIS));
        return relativeTime.before(currentTime);
    }

    public synchronized void markRefreshComplete(boolean updateSuccess) {
        if (updateSuccess) {
            Cache timeCache = cacheManager.getCache(STORY_REFRESH_CACHE);
            if(timeCache == null) {
                timeCache = new ConcurrentMapCache(STORY_REFRESH_CACHE);
            }
            Date lastRefreshTime = new Date();
            timeCache.put(LAST_REFRESH_TIME, lastRefreshTime);
            logger.debug("Stories refreshed at {}", lastRefreshTime);
        }
    }
}
