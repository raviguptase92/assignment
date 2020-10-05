package com.insider.assignment.controller;

import com.insider.assignment.constant.Constants;
import com.insider.assignment.dto.StoryDTO;
import com.insider.assignment.service.AsyncStoryUpdateService;
import com.insider.assignment.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StoryController {

    @Autowired
    private StoryService storyService;

    @Autowired
    private AsyncStoryUpdateService asyncStoryUpdateService;

    @GetMapping("/top-stories")
    @ResponseBody
    public List<StoryDTO> topStories() {
//        if(StoryRefreshMetadata.needUpdate()) {
//            asyncStoryUpdateService.raiseUpdateEvent();
//        }
        return storyService.getTopStories();
    }

    @GetMapping("/past-stories")
    @ResponseBody
    @Cacheable(Constants.PAST_STORY_CACHE)
    public List<StoryDTO> pastStories() {
        return storyService.getPastStories();
    }
}
