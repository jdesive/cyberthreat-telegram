package com.jdesive.cyberthreattelegram.service;

import com.jdesive.cyberthreattelegram.RSSReaderUtil;
import com.jdesive.cyberthreattelegram.config.TelegramBot;
import com.jdesive.cyberthreattelegram.pojo.Article;
import com.jdesive.cyberthreattelegram.pojo.Feed;
import com.jdesive.cyberthreattelegram.repository.ArticleRepository;
import com.jdesive.cyberthreattelegram.repository.FeedRepository;
import com.rometools.rome.io.FeedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ArticleService {

    private final FeedRepository feedRepository;
    private final ArticleRepository articleRepository;
    private final TelegramBot telegramBot;

    @Autowired
    public ArticleService(FeedRepository feedRepository, ArticleRepository articleRepository, TelegramBot telegramBot) {
        this.feedRepository = feedRepository;
        this.articleRepository = articleRepository;
        this.telegramBot = telegramBot;
    }

    @Scheduled(cron = "0 0 0-23 * * *")
    public void poll() {
        List<Feed> feeds = this.feedRepository.findAll();
        feeds.forEach(this::pollFeed);
    }

    public Feed getFeedById(long id) {
        return this.feedRepository.getReferenceById(id);
    }

    public Feed getFeedByName(String name) {
        return this.feedRepository.findByName(name);
    }

    public List<Feed> getFeeds() {
        return this.feedRepository.findAll();
    }

    public void pollFeed(Feed feed) {
        try {
            List<Article> results = RSSReaderUtil.read(feed.getUrl(), feed.getName());
            log.info("Polling RSS feed: {} {}", feed.getName(), feed.getUrl());
            results.forEach(a -> {
                if (!this.articleRepository.existsByLink(a.getLink())) {
                    log.info("[{}] Found article: {}", feed.getName(), a);
                    this.telegramBot.sendMessage(a);
                    this.articleRepository.save(a);
                }
            });
        } catch (IOException | FeedException ex) {
            ex.printStackTrace();
        }
    }

    public Feed addFeed(Feed feed) {
        return this.feedRepository.save(feed);
    }

}
