package ru.roafo.telegrambot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.roafo.telegrambot.service.SendBotMessageService;

import static ru.roafo.telegrambot.command.CommandName.*;

@Component
public class HelpCommand implements Command {
    private final SendBotMessageService sendBotMessageService;

    public static final String HELP_MESSAGE = String.format("✨<b>Дотупные команды</b>✨\n\n"

                    + "<b>Начать\\закончить работу с ботом:</b>\n"
                    + "%s - начать работу со мной\n"
                    + "%s - приостановить работу со мной\n\n"

                    + "Работа с подписками на группы:\n"
                    + "%s или %s - подписаться на группу статей\n"
                    + "%s - получить список групп по оформленным подпискам\n\n"

                    + "%s - получить статистику использования\n"
                    + "%s - получить помощь в работе со мной\n"
                   ,
            START.getCommandName(),
            STOP.getCommandName(),
            ADD_GROUP_SUB.getCommandName(),
            ADD_GROUP_SUB_RUS.getCommandName(),
            LIST_GROUP_SUB.getCommandName(),
            STAT.getCommandName(),
            HELP.getCommandName()
    );

    @Autowired
    public HelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), HELP_MESSAGE);
    }

    @Override
    public void unExecute() {

    }
}
