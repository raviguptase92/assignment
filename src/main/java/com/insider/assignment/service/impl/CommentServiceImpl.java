package com.insider.assignment.service.impl;

import com.insider.assignment.constant.Constants;
import com.insider.assignment.dto.CommentDTO;
import com.insider.assignment.dto.hn.HNItem;
import com.insider.assignment.dto.hn.HNUser;
import com.insider.assignment.service.CommentService;
import com.insider.assignment.service.HttpService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.*;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private HttpService httpService;

    private ExecutorService executorService;

    private static final Logger logger = LogManager.getLogger(CommentServiceImpl.class);

    @PostConstruct
    void init() {
        executorService = Executors.newFixedThreadPool(Constants.EXECUTOR_THREAD_COUNT);
    }

    @Override
    public List<CommentDTO> getComments(Integer storyId) throws ExecutionException, InterruptedException {
        HNItem hnStory = httpService.makeGetRequest(String.format(Constants.ITEM_API, storyId), HNItem.class);
        List<Integer> storyComments = hnStory.getKids();
        List<Future<HNItem>> hnComments = new ArrayList<>();
        Map<String, HNUser> userMap = new ConcurrentHashMap<>();
        for(int i=0; i < 10 && i < storyComments.size() ; i++) {
            int finalI = i;
            Future<HNItem> hnItemFuture = executorService.submit(() -> {
                String commentUrl = String.format(Constants.ITEM_API, storyComments.get(finalI));
                HNItem hnComment = httpService.makeGetRequest(commentUrl, HNItem.class);
                HNUser hnUser;
                if (!userMap.containsKey(hnComment.getBy())) {
                    String userApi = String.format(Constants.USER_API, hnComment.getBy());
                    hnUser = httpService.makeGetRequest(userApi, HNUser.class);
                    if (hnUser != null) {
                        hnUser.calculateAge();
                        userMap.put(hnUser.getId(), hnUser);
                    } else {
                        logger.error("Unable to find HNUser with id {}", hnComment.getBy());
                    }
                }
                return hnComment;
            });
            hnComments.add(hnItemFuture);
        }
        return getCommentsFromFutureObjects(hnComments, userMap);
    }

    private List<CommentDTO> getCommentsFromFutureObjects(List<Future<HNItem>> hnComments, Map<String, HNUser> userMap) throws ExecutionException, InterruptedException {
        List<CommentDTO> comments = new ArrayList<>();
        for (Future<HNItem> hnItem: hnComments) {
            HNItem hnComment = hnItem.get();
            HNUser hnUser = userMap.get(hnComment.getBy());
            CommentDTO comment = new CommentDTO();
            comment.setText(hnComment.getText());
            if (hnUser != null) {
                comment.setHnHandle(hnUser.getId());
                comment.setHnAge(hnUser.getAge());
            }
            comments.add(comment);
        }
        return comments;
    }
}
