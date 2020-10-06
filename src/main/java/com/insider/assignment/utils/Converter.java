package com.insider.assignment.utils;

import com.insider.assignment.dto.hn.HNItem;
import com.insider.assignment.dto.StoryDTO;
import com.insider.assignment.entity.Story;
import com.insider.assignment.entity.TopStory;

import java.util.Date;

public class Converter {

    public static StoryDTO buildStoryDTOFromHNStory(HNItem hnStory) {
        if(hnStory == null) {
            return null;
        }
        return getStoryDTO(hnStory.getId(), hnStory.getTitle(), hnStory.getUrl(), hnStory.getScore(), hnStory.getTime(), hnStory.getBy());
    }

    public static StoryDTO buildStoryDTOFromEntity(Story story) {
        if(story == null) {
            return null;
        }
        return getStoryDTO(story.getId(), story.getTitle(), story.getUrl(), story.getScore(), story.getTimeOfSubmission(), story.getAuthor());
    }

    public static StoryDTO buildStoryDTOFromTopStory(TopStory topStory) {
        if(topStory == null) {
            return null;
        }
        return getStoryDTO(topStory.getId(), topStory.getTitle(), topStory.getUrl(), topStory.getScore(), topStory.getTimeOfSubmission(), topStory.getAuthor());
    }

    public static TopStory buildTopStoryFromDTO(StoryDTO storyDTO) {
        if(storyDTO == null) {
            return null;
        }
        return getTopStory(storyDTO.getId(), storyDTO.getTitle(), storyDTO.getUrl(), storyDTO.getScore(), storyDTO.getTimeOfSubmission(), storyDTO.getAuthor());
    }

    public static Story buildStoryFromTopStory(TopStory topStory) {
        if(topStory == null) {
            return null;
        }
        return getStory(topStory.getId(), topStory.getTitle(), topStory.getUrl(), topStory.getScore(), topStory.getTimeOfSubmission(), topStory.getAuthor());
    }

    private static Story getStory(Long id, String title, String url, Integer score, Date timeOfSubmission, String author) {
        Story entity = new Story();
        entity.setId(id);
        entity.setTitle(title);
        entity.setUrl(url);
        entity.setScore(score);
        entity.setTimeOfSubmission(timeOfSubmission);
        entity.setAuthor(author);
        return entity;
    }

    private static TopStory getTopStory(Long id, String title, String url, Integer score, Date timeOfSubmission, String author) {
        TopStory entity = new TopStory();
        entity.setId(id);
        entity.setTitle(title);
        entity.setUrl(url);
        entity.setScore(score);
        entity.setTimeOfSubmission(timeOfSubmission);
        entity.setAuthor(author);
        return entity;
    }

    private static StoryDTO getStoryDTO(Long id, String title, String url, Integer score, Date timeOfSubmission, String author) {
        StoryDTO storyDTO = new StoryDTO();
        storyDTO.setId(id);
        storyDTO.setTitle(title);
        storyDTO.setUrl(url);
        storyDTO.setScore(score);
        storyDTO.setTimeOfSubmission(timeOfSubmission);
        storyDTO.setAuthor(author);
        return storyDTO;
    }
}
