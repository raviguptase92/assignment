package com.insider.assignment.service;

public interface AsyncStoryUpdateService {

    void raiseUpdateEvent(long delay);

    void updateTopStories() throws Exception;
}
