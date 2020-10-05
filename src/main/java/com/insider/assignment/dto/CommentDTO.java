package com.insider.assignment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CommentDTO {
    private String text;
    private String hnHandle;
    private Integer hnAge;
    private List<CommentDTO> childComments;
}
