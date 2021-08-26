package ru.roafo.telegrambot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.roafo.telegrambot.service.SendBotMessageService;

import javax.annotation.PostConstruct;

import java.nio.charset.StandardCharsets;

import static ru.roafo.telegrambot.command.CommandName.*;

@Component
public class HelpCommand implements Command {

    private SendBotMessageService sendBotMessageService;

    @Value("${message.help}")
    private String helpMessageTemplate;

    private String helpMessage ;

    @Autowired
    public void setSendBotMessageService(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
        helpMessage = String.format(new String(helpMessageTemplate.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8),
                START.getCommandName(),
                STOP.getCommandName(),
                ADD_GROUP_SUB.getCommandName(),
                ADD_GROUP_SUB_RUS.getCommandName(),
                LIST_GROUP_SUB.getCommandName(),
                STAT.getCommandName(),
                HELP.getCommandName());
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), helpMessage);
    }

    @Override
    public void unExecute() {

    }
}
