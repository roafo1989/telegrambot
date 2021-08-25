package ru.roafo.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.roafo.telegrambot.bot.JavarushTelegramBot;

import java.util.List;

@Service
public class SendBotMessageServiceImpl implements SendBotMessageService {

    private JavarushTelegramBot bot;

    @Autowired
    public void setBot(JavarushTelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(message);
        try {
            bot.execute(sendMessage);
        } catch (Exception e) {
            System.out.println("TelegramApiException");
        }
    }

    @Override
    public void sendMessage(String chatId, List<String> messages) {
        for (String message : messages) {
            sendMessage(chatId, message);
        }
    }
}
