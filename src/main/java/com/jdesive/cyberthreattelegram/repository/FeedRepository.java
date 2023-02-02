package com.jdesive.cyberthreattelegram.repository;

import com.jdesive.cyberthreattelegram.pojo.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    Feed findByName(String name);

}
