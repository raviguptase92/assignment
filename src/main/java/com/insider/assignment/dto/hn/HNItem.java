package com.insider.assignment.dto.hn;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.insider.assignment.utils.UnixTimeStampDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HNItem {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("url")
    private String url;

    //@JsonFormat(shape=JsonFormat.Shape.NUMBER, pattern="s")
    @JsonDeserialize(using = UnixTimeStampDeserializer.class)
    @JsonProperty("time")
    private Date time;

    @JsonProperty("score")
    private Integer score;

    @JsonProperty("descendants")
    private Integer descendants;

    @JsonProperty("kids")
    private List<Integer> kids;

    @JsonProperty("text")
    private String text;

    @JsonProperty("by")
    private String by;
}
