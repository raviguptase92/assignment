package com.insider.assignment.service.impl;

import com.insider.assignment.constant.Constants;
import com.insider.assignment.dto.StoryDTO;
import com.insider.assignment.entity.Story;
import com.insider.assignment.entity.TopStory;
import com.insider.assignment.repository.StoryRepository;
import com.insider.assignment.repository.TopStoryRepository;
import com.insider.assignment.service.StoryService;
import com.insider.assignment.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames={"story"})
public class StoryServiceImpl implements StoryService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private TopStoryRepository topStoryRepository;

    @Override
    @Cacheable(value=Constants.TOP_STORY_CACHE, unless = "#result==null or #result.size()==0")
    public List<StoryDTO> getTopStories() {
        List<TopStory> storyList = topStoryRepository.findTop10ByOrderByScoreDesc();
        List<StoryDTO> top10Stories = new ArrayList<>();
        if(CollectionUtils.isEmpty(storyList)) {
            return top10Stories;
        }

        for (TopStory topStory: storyList) {
            top10Stories.add(Converter.buildStoryDTOFromTopStory(topStory));
        }
        return top10Stories;
    }

    @Override
    public List<StoryDTO> getPastStories() {
        List<Story> pastStories = storyRepository.findAll();
        List<StoryDTO> storyDTOS = new ArrayList<>();
        if(CollectionUtils.isEmpty(pastStories)) {
            return storyDTOS;
        }

        for (Story story: pastStories) {
            storyDTOS.add(Converter.buildStoryDTOFromEntity(story));
        }
        return storyDTOS;
    }
}
