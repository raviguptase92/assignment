package com.insider.assignment.constant;

public class Constants {

    // Hacker news API(s)
    public static final String TOP_STORY_API = "https://hacker-news.firebaseio.com/v0/topstories.json";
    public static final String ITEM_API = "https://hacker-news.firebaseio.com/v0/item/%s.json";
    public static final String USER_API = "https://hacker-news.firebaseio.com/v0/user/%s.json";

    // Cache names
    public static final String TOP_STORY_CACHE = "top_stories";
    public static final String PAST_STORY_CACHE = "past_stories";
    public static final String COMMENT_CACHE = "story_comments";

    public static final long ONE_MINUTE_IN_MILLIS = 60000;
    public static final int EXECUTOR_THREAD_COUNT = 16;
}
