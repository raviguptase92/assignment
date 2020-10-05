package com.insider.assignment.service;

import com.insider.assignment.dto.StoryDTO;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;

public interface StoryService {
    List<StoryDTO> getTopStories();

    List<StoryDTO> getPastStories();
}
