package ru.roafo.telegrambot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.roafo.telegrambot.service.SendBotMessageService;

import static ru.roafo.telegrambot.command.CommandName.STAT;
import static java.lang.String.*;

/**
* Admin Help {@link Command}.
*/
public class AdminHelpCommand implements Command {

   public static final String ADMIN_HELP_MESSAGE = format("✨<b>Доступные команды админа</b>✨\n\n"
                   + "<b>Получить статистику</b>\n"
                   + "%s - статистика бота\n",
           STAT.getCommandName());

   private final SendBotMessageService sendBotMessageService;

   public AdminHelpCommand(SendBotMessageService sendBotMessageService) {
       this.sendBotMessageService = sendBotMessageService;
   }

   @Override
   public void execute(Update update) {
       sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), ADMIN_HELP_MESSAGE);
   }

    @Override
    public void unExecute() {

    }
}