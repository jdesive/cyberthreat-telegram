package com.jdesive.cyberthreattelegram.config;

import com.jdesive.cyberthreattelegram.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {

    private ArticleService articleService;

    @Autowired
    public ApplicationRunner(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        this.articleService.poll();

    }
}
