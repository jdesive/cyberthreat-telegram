package com.jdesive.cyberthreattelegram.controller;

import com.jdesive.cyberthreattelegram.pojo.Feed;
import com.jdesive.cyberthreattelegram.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArticleController {

    private ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/feed")
    public ResponseEntity<Feed> addFeed(@RequestBody Feed feed) {
        return ResponseEntity.ok(this.articleService.addFeed(feed));
    }

    @GetMapping("/feed")
    public ResponseEntity<List<Feed>> listFeeds() {
        return ResponseEntity.ok(this.articleService.getFeeds());
    }

    @GetMapping("/poll")
    public ResponseEntity<String> pollById() {
        this.articleService.poll();
        return ResponseEntity.ok("Polling");
    }

    @GetMapping("/feed/{id}/poll")
    public ResponseEntity<String> pollById(@PathVariable("id") long id) {
        this.articleService.pollFeed(this.articleService.getFeedById(id));
        return ResponseEntity.ok("Polling");
    }

    @GetMapping("/feed/{name}/poll")
    public ResponseEntity<String> pollByName(@PathVariable("name") String name) {
        this.articleService.pollFeed(this.articleService.getFeedByName(name));
        return ResponseEntity.ok("Polling");
    }


}
