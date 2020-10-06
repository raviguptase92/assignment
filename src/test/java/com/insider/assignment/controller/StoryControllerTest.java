package com.insider.assignment.controller;

import com.insider.assignment.dto.StoryDTO;
import com.insider.assignment.service.StoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class StoryControllerTest {

    @InjectMocks
    private StoryController storyController = new StoryController();

    @Mock
    private StoryService storyService;

    @Before
    public void setUp() {
        storyService = Mockito.mock(StoryService.class);
        ReflectionTestUtils.setField(storyController, "storyService", storyService);
    }

    @Test
    public void testTopStoriesWhenListIsEmpty() {
        List<StoryDTO> stories = storyController.topStories();
        Assertions.assertEquals(0, stories.size());
    }

    @Test
    public void testTopStoriesWhenListIsNonEmpty() {
        List<StoryDTO> topStories = new ArrayList<>();
        topStories.add(new StoryDTO());
        Mockito.when(storyService.getTopStories()).thenReturn(topStories);
        List<StoryDTO> stories = storyController.topStories();
        Assertions.assertEquals(1, stories.size());
    }

    @Test
    public void testPastStoriesWhenListIsEmpty() {
        List<StoryDTO> stories = storyController.pastStories();
        Assertions.assertEquals(0, stories.size());
    }

    @Test
    public void testPastStoriesWhenListIsNonEmpty() {
        List<StoryDTO> pastStories = new ArrayList<>();
        pastStories.add(new StoryDTO());
        Mockito.when(storyService.getPastStories()).thenReturn(pastStories);
        List<StoryDTO> stories = storyController.pastStories();
        Assertions.assertEquals(1, stories.size());
    }
}
