package com.insider.assignment.service.impl;

import com.insider.assignment.dto.StoryDTO;
import com.insider.assignment.entity.Story;
import com.insider.assignment.entity.TopStory;
import com.insider.assignment.repository.StoryRepository;
import com.insider.assignment.repository.TopStoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StoryServiceImplTest {

    @InjectMocks
    private StoryServiceImpl storyService = new StoryServiceImpl();

    @Mock
    private StoryRepository storyRepository;

    @Mock
    private TopStoryRepository topStoryRepository;

    @Before
    public void setUp() {
        topStoryRepository = Mockito.mock(TopStoryRepository.class);
        storyRepository = Mockito.mock(StoryRepository.class);
        ReflectionTestUtils.setField(storyService, "topStoryRepository", topStoryRepository);
        ReflectionTestUtils.setField(storyService, "storyRepository", storyRepository);
    }

    @Test
    public void testGetTopStoriesWhenNoEntryPresentInDB() {
        Mockito.when(topStoryRepository.findTop10ByOrderByScoreDesc()).thenReturn(null);
        List<StoryDTO> topStories = storyService.getTopStories();
        Assertions.assertEquals(0, topStories.size());
    }

    @Test
    public void testGetTopStoriesWhenEntryPresentInDB() {
        ArrayList<TopStory> persistedStories = new ArrayList<>();
        TopStory story = new TopStory();
        story.setId(123L);
        story.setTitle("My first story");
        story.setScore(102);
        story.setTimeOfSubmission(new Date());
        story.setAuthor("Ravi");
        persistedStories.add(story);
        Mockito.when(topStoryRepository.findTop10ByOrderByScoreDesc()).thenReturn(persistedStories);
        List<StoryDTO> topStories = storyService.getTopStories();
        Assertions.assertEquals(1, topStories.size());
    }

    @Test
    public void testGetPastStoriesWhenNoEntryPresentInDB() {
        Mockito.when(storyRepository.findAll()).thenReturn(null);
        List<StoryDTO> topStories = storyService.getPastStories();
        Assertions.assertEquals(0, topStories.size());
    }

    @Test
    public void testGetPastStoriesWhenEntryPresentInDB() {
        ArrayList<Story> persistedStories = new ArrayList<>();
        Story story = new Story();
        story.setId(123L);
        story.setTitle("My first story");
        story.setScore(102);
        story.setTimeOfSubmission(new Date());
        story.setAuthor("Ravi");
        persistedStories.add(story);
        Mockito.when(storyRepository.findAll()).thenReturn(persistedStories);
        List<StoryDTO> topStories = storyService.getPastStories();
        Assertions.assertEquals(1, topStories.size());
    }
}
