package com.insider.assignment.repository;

import com.insider.assignment.entity.TopStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopStoryRepository extends JpaRepository<TopStory, Long> {

    List<TopStory> findTop10ByOrderByScoreDesc();
}
