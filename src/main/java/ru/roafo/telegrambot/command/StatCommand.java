package ru.roafo.telegrambot.command;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.roafo.telegrambot.command.annotation.AdminCommand;
import ru.roafo.telegrambot.service.SendBotMessageService;
import ru.roafo.telegrambot.service.TelegramUserService;

import java.nio.charset.StandardCharsets;

@AdminCommand
@Component
public class StatCommand implements Command {

    @Value("${message.stat}")
    public String statMessage;

    private final TelegramUserService telegramUserService;
    private final SendBotMessageService sendBotMessageService;

    @Autowired
    public StatCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }
    
    @Override
    public void execute(Update update) {
        long activeUserCount = telegramUserService.countAllByActiveTrue();
        sendBotMessageService.sendMessage(CommandUtils.getChatId(update), String.format(new String(statMessage.getBytes(StandardCharsets.ISO_8859_1)), activeUserCount));
    }

    @Override
    public void unExecute() {

    }
}
