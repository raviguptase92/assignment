package com.insider.assignment.controller;

import com.insider.assignment.constant.Constants;
import com.insider.assignment.dto.StoryDTO;
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

    @GetMapping("/top-stories")
    @ResponseBody
    public List<StoryDTO> topStories() {
        return storyService.getTopStories();
    }

    @GetMapping("/past-stories")
    @ResponseBody
    @Cacheable(value=Constants.PAST_STORY_CACHE, unless = "#result==null or #result.size()==0")
    public List<StoryDTO> pastStories() {
        return storyService.getPastStories();
    }
}
