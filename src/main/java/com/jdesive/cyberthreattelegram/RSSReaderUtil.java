package com.jdesive.cyberthreattelegram;

import com.jdesive.cyberthreattelegram.pojo.Article;
import com.rometools.modules.mediarss.MediaEntryModule;
import com.rometools.modules.mediarss.types.MediaContent;
import com.rometools.rome.feed.module.Module;
import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndCategoryImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class RSSReaderUtil {

    public static List<Article> read(String feedUrl, String feedName) throws IOException, FeedException {
        URL feedSource = new URL(feedUrl);
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(feedSource));
        Iterator itr = feed.getEntries().iterator();
        List<Article> results = new ArrayList<>();
        while (itr.hasNext()) {
            SyndEntry syndEntry = (SyndEntry) itr.next();
            results.add(mapToArticle(syndEntry, feedName));
        }

        return results;
    }

    /**
     * Map to Article
     * @param syndEntry
     */
    private static Article mapToArticle(SyndEntry syndEntry, String feedName) {

        Article article = new Article();
        article.setTitle(syndEntry.getTitle());
        article.setPublishedDate(syndEntry.getPublishedDate().toString());
        article.setCategories(syndEntry.getCategories().stream().map(SyndCategory::getName).collect(Collectors.toList()));
        article.setLink(syndEntry.getLink());
        article.setDescription(syndEntry.getDescription().getValue());
        article.setSource(feedName);

        for (Module module : syndEntry.getModules()) {
            if (module instanceof MediaEntryModule) {
                MediaEntryModule media = (MediaEntryModule) module;
                for (MediaContent mediaContent : media.getMediaContents()) {
                    if (Arrays.stream(mediaContent.getMetadata().getKeywords()).anyMatch(item -> item.equalsIgnoreCase("full"))) {
                        article.setImgUrl(mediaContent.getReference().toString());
                    }
                }
            }
        }

        return article;
    }
}