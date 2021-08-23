package ru.roafo.telegrambot.service;

import java.util.List;

public interface SendBotMessageService {

    /**
     * Send message via telegram bot.
     *
     * @param chatId provided chatId in which messages would be sent.
     * @param message provided message to be sent.
     */
    void sendMessage(String chatId, String message);

    void sendMessage(String chatId, List<String> messages);
}
