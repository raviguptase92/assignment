package com.insider.assignment.service.impl;

import com.insider.assignment.constant.Constants;
import com.insider.assignment.dto.CommentDTO;
import com.insider.assignment.dto.hn.HNItem;
import com.insider.assignment.dto.hn.HNUser;
import com.insider.assignment.service.HttpService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService = new CommentServiceImpl();

    @Mock
    private HttpService httpService;

    @Before
    public void setUp() {
        httpService = Mockito.mock(HttpService.class);
        ReflectionTestUtils.setField(commentService, "httpService", httpService);
        ReflectionTestUtils.setField(commentService, "executorService", Executors.newFixedThreadPool(Constants.EXECUTOR_THREAD_COUNT));
    }

    @Test
    public void testGetCommentsWhenStoryIsNull() throws ExecutionException, InterruptedException {
        List<CommentDTO> comments = commentService.getComments(null);
        Assertions.assertEquals(0, comments.size());
    }

    @Test
    public void testGetCommentsWhenStoryIsInvalid() throws ExecutionException, InterruptedException {
        List<CommentDTO> comments = commentService.getComments(123);
        Assertions.assertEquals(0, comments.size());
    }

    @Test
    public void testGetCommentsWhenStoryIsValid() throws ExecutionException, InterruptedException {
        int storyId = 123;
        HNItem hnStory = new HNItem();
        List<Integer> storyComments = new ArrayList<>();
        storyComments.add(1234);
        storyComments.add(5678);
        hnStory.setId(123L);
        hnStory.setKids(storyComments);

        HNItem hnComment1 = new HNItem();
        hnComment1.setId(1234L);
        hnComment1.setText("Hakuna matata");
        hnComment1.setBy("alpha");
        HNItem hnComment2 = new HNItem();
        hnComment2.setId(5678L);
        hnComment2.setText("Hakuna why matata");
        hnComment2.setBy("beta");

        HNUser alpha = new HNUser();
        alpha.setId("alpha");
        Mockito.when(httpService.makeGetRequest(String.format(Constants.ITEM_API, storyId), HNItem.class)).thenReturn(hnStory);
        Mockito.when(httpService.makeGetRequest(String.format(Constants.ITEM_API, storyComments.get(0)), HNItem.class)).thenReturn(hnComment1);
        Mockito.when(httpService.makeGetRequest(String.format(Constants.ITEM_API, storyComments.get(1)), HNItem.class)).thenReturn(hnComment2);
        Mockito.when(httpService.makeGetRequest(String.format(Constants.USER_API, "alpha"), HNUser.class)).thenReturn(alpha);
        Mockito.when(httpService.makeGetRequest(String.format(Constants.USER_API, "beta"), HNUser.class)).thenReturn(null);
        List<CommentDTO> comments = commentService.getComments(storyId);
        Assertions.assertEquals(2, comments.size());
    }

    @Test
    public void testGetCommentsWhenStoryIsValidButCommentIsInvalid() throws ExecutionException, InterruptedException {
        int storyId = 123;
        HNItem hnStory = new HNItem();
        List<Integer> storyComments = new ArrayList<>();
        storyComments.add(1234);
        storyComments.add(5678);
        hnStory.setId(123L);
        hnStory.setKids(storyComments);

        HNItem hnComment = new HNItem();
        hnComment.setId(1234L);
        hnComment.setText("Hakuna matata");
        hnComment.setBy("alpha");

        HNUser alpha = new HNUser();
        alpha.setId("alpha");
        Mockito.when(httpService.makeGetRequest(String.format(Constants.ITEM_API, storyId), HNItem.class)).thenReturn(hnStory);
        Mockito.when(httpService.makeGetRequest(String.format(Constants.ITEM_API, storyComments.get(0)), HNItem.class)).thenReturn(hnComment);
        Mockito.when(httpService.makeGetRequest(String.format(Constants.ITEM_API, storyComments.get(1)), HNItem.class)).thenReturn(null);
        Mockito.when(httpService.makeGetRequest(String.format(Constants.USER_API, "alpha"), HNUser.class)).thenReturn(alpha);
        List<CommentDTO> comments = commentService.getComments(storyId);
        Assertions.assertEquals(1, comments.size());
    }
}
