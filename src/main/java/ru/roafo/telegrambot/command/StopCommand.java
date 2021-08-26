package ru.roafo.telegrambot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.roafo.telegrambot.service.SendBotMessageService;
import ru.roafo.telegrambot.service.TelegramUserService;

import java.nio.charset.StandardCharsets;

/**
* Stop {@link Command}.
*/
@Component
public class StopCommand implements Command {

    @Value("${message.stop}")
    private String stopMessage;
    private final SendBotMessageService sendBotMessageService;
    private TelegramUserService telegramUserService;

    @Autowired
    public StopCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        sendBotMessageService.sendMessage(chatId, new String(stopMessage.getBytes(StandardCharsets.ISO_8859_1)));

        telegramUserService.findByChatId(chatId)
                .ifPresent(it -> {
                    it.setActive(false);
                    telegramUserService.save(it);
                });
    }

    @Override
    public void unExecute() {

    }
}