package ru.roafo.telegrambot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.roafo.telegrambot.service.SendBotMessageService;

import java.nio.charset.StandardCharsets;

import static java.lang.String.format;
import static ru.roafo.telegrambot.command.CommandName.STAT;

/**
 * Admin Help {@link Command}.
 */
@Component
public class AdminHelpCommand implements Command {

    @Value("${message.adminHelpMessage}")
    private String adminHelpMessageTemplate;

    private String adminHelpMessage;

    private SendBotMessageService sendBotMessageService;

    @Autowired
    public void setSendBotMessageService(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
        adminHelpMessage = format(new String(adminHelpMessageTemplate.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), STAT.getCommandName());
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), adminHelpMessage);
    }

    @Override
    public void unExecute() {

    }
}