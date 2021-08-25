package ru.roafo.telegrambot.command;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.roafo.telegrambot.command.annotation.AdminCommand;
import ru.roafo.telegrambot.service.SendBotMessageService;
import ru.roafo.telegrambot.service.TelegramUserService;

@AdminCommand
@Component
public class StatCommand implements Command {

    private final TelegramUserService telegramUserService;
    private final SendBotMessageService sendBotMessageService;

    public String statMessage = "Javarush Telegram Bot использует %s человек.";

    @Autowired
    public StatCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }
    
    @Override
    public void execute(Update update) {
        long activeUserCount = telegramUserService.countAllByActiveTrue();
        sendBotMessageService.sendMessage(CommandUtils.getChatId(update), String.format(statMessage, activeUserCount));
    }

    @Override
    public void unExecute() {

    }
}
