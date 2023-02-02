package com.jdesive.cyberthreattelegram.config;

import com.jdesive.cyberthreattelegram.pojo.Article;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.chat.id}")
    private String chatId;

    public TelegramBot() {
    }

    public void sendMessage(Article article) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();

        InlineKeyboardButton tmdb = new InlineKeyboardButton("View in browser");
        tmdb.setUrl(article.getLink());

        buttons1.add(tmdb);
        keyboard.add(buttons1);
        inlineKeyboard.setKeyboard(keyboard);

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(this.chatId);
        sendPhoto.setPhoto(new InputFile(article.getImgUrl()));
        sendPhoto.setCaption("*" + article.getSource() + " - " + article.getTitle() + "*\n\n" + article.getDescription());
        sendPhoto.setReplyMarkup(inlineKeyboard);
        sendPhoto.setParseMode("markdown");
        try {
            this.execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {

    }
}
