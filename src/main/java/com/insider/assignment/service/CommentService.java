package com.insider.assignment.service;

import com.insider.assignment.dto.CommentDTO;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface CommentService {
    List<CommentDTO> getComments(Integer storyId) throws ExecutionException, InterruptedException;
}
