package com.insider.assignment.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity
public class TopStory {

    @Id
    private Long id;
    private String title;
    private String url;
    private Integer score;
    private Date timeOfSubmission;
    private Date availableAfter;
    private String author;
}
