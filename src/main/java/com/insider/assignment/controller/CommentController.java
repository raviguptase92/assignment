package com.insider.assignment.controller;

import com.insider.assignment.constant.Constants;
import com.insider.assignment.dto.CommentDTO;
import com.insider.assignment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/comments")
    @ResponseBody
    @Cacheable(Constants.COMMENT_CACHE)
    public List<CommentDTO> comments(@RequestParam("storyId") Integer storyId) throws Exception {
        return commentService.getComments(storyId);
    }
}
