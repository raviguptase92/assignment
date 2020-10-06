package com.insider.assignment.utils;

import com.insider.assignment.dto.StoryDTO;
import com.insider.assignment.dto.hn.HNItem;
import com.insider.assignment.entity.Story;
import com.insider.assignment.entity.TopStory;
import org.junit.Assert;
import org.junit.Test;

public class ConverterTest {

    @Test
    public void testBuildStoryDTOFromHNStoryWithNull() {
        StoryDTO dto = Converter.buildStoryDTOFromHNStory(null);
        Assert.assertNull(dto);
    }

    @Test
    public void testBuildStoryDTOFromHNStoryWithNotNull() {
        HNItem item = new HNItem();
        item.setId(1L);
        StoryDTO dto = Converter.buildStoryDTOFromHNStory(item);
        Assert.assertEquals(dto.getId(), item.getId());
    }

    @Test
    public void testBuildStoryDTOFromEntityWithNull() {
        StoryDTO dto = Converter.buildStoryDTOFromEntity(null);
        Assert.assertNull(dto);
    }

    @Test
    public void testBuildStoryDTOFromEntityWithNotNull() {
        Story item = getStory(1L, "Title", null, 101, "Ravi");
        StoryDTO expected = Converter.buildStoryDTOFromEntity(item);
        Assert.assertEquals(expected.getId(), item.getId());
        Assert.assertEquals(expected.getTitle(), "Title");
        Assert.assertNull(expected.getUrl());
        Assert.assertEquals(101, (int) expected.getScore());
    }

    @Test
    public void testBuildStoryDTOFromTopStoryWithNull() {
        StoryDTO dto = Converter.buildStoryDTOFromTopStory(null);
        Assert.assertNull(dto);
    }

    @Test
    public void testBuildStoryDTOFromTopStoryWithNotNull() {
        TopStory item = getTopStory(1L, "Title", null, 101, "Ravi");
        StoryDTO expected = Converter.buildStoryDTOFromTopStory(item);
        Assert.assertEquals(expected.getId(), item.getId());
        Assert.assertEquals(expected.getTitle(), "Title");
        Assert.assertNull(expected.getUrl());
        Assert.assertEquals(101, (int) expected.getScore());
    }

    @Test
    public void testBuildTopStoryFromDTOWithNull() {
        TopStory topStory = Converter.buildTopStoryFromDTO(null);
        Assert.assertNull(topStory);
    }

    @Test
    public void testBuildTopStoryFromDTOWithNotNull() {
        StoryDTO item = getStoryDTO(1L, "Title", null, 101, "Ravi");
        TopStory expected = Converter.buildTopStoryFromDTO(item);
        Assert.assertEquals(expected.getId(), item.getId());
        Assert.assertEquals(expected.getTitle(), "Title");
        Assert.assertNull(expected.getUrl());
        Assert.assertEquals(101, (int) expected.getScore());
    }

    @Test
    public void testBuildStoryFromTopStoryWithNull() {
        Story story = Converter.buildStoryFromTopStory(null);
        Assert.assertNull(story);
    }

    @Test
    public void testBuildStoryFromTopStoryWithNotNull() {
        TopStory item = getTopStory(1L, "Title", null, 101, "Ravi");
        Story expected = Converter.buildStoryFromTopStory(item);
        Assert.assertEquals(expected.getId(), item.getId());
        Assert.assertEquals(expected.getTitle(), "Title");
        Assert.assertNull(expected.getUrl());
        Assert.assertEquals(101, (int) expected.getScore());
    }

    private TopStory getTopStory(Long id, String title, String url, int score, String author) {
        TopStory topStory = new TopStory();
        topStory.setId(id);
        topStory.setTitle(title);
        topStory.setUrl(url);
        topStory.setScore(score);
        topStory.setAuthor(author);
        return topStory;
    }

    private Story getStory(Long id, String title, String url, int score, String author) {
        Story story = new Story();
        story.setId(id);
        story.setTitle(title);
        story.setUrl(url);
        story.setScore(score);
        story.setAuthor(author);
        return story;
    }

    private StoryDTO getStoryDTO(Long id, String title, String url, int score, String author) {
        StoryDTO story = new StoryDTO();
        story.setId(id);
        story.setTitle(title);
        story.setUrl(url);
        story.setScore(score);
        story.setAuthor(author);
        return story;
    }
}
