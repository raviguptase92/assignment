package com.insider.assignment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
public class StoryDTO implements Comparable<StoryDTO> {
    private Long id;
    private String title;
    private String url;
    private Integer score;
    private Date timeOfSubmission;
    private String author;

    @Override
    public int compareTo(StoryDTO another) {
        if(this.score == null && another.score == null) {
            return 0;
        }
        return another.getScore() - Objects.requireNonNullElse(this.score, 0);
    }
}
