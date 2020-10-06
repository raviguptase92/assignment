package com.insider.assignment.controller;

import com.insider.assignment.dto.CommentDTO;
import com.insider.assignment.service.CommentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

public class CommentControllerTest {

    @InjectMocks
    private CommentController commentController = new CommentController();

    @Mock
    private CommentService commentService;

    @Before
    public void setUp() {
        commentService = Mockito.mock(CommentService.class);
        ReflectionTestUtils.setField(commentController, "commentService", commentService);
    }

    @Test
    public void testCommentsWhenReturnEmptyList() throws Exception {
        when(commentService.getComments(Mockito.anyInt())).thenReturn(new ArrayList<>());
        List result1 = commentController.comments(123);

        List<CommentDTO> commentDTOS = new ArrayList<>();
        commentDTOS.add(new CommentDTO());
        when(commentService.getComments(Mockito.anyInt())).thenReturn(commentDTOS);
        List result2 = commentController.comments(123);

        assertNotEquals(result1.size(), result2.size());
    }
}
