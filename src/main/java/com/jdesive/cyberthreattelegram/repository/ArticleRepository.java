package com.jdesive.cyberthreattelegram.repository;

import com.jdesive.cyberthreattelegram.pojo.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    boolean existsByLink(String link);

}
