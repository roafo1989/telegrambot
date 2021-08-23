package ru.roafo.telegrambot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.roafo.telegrambot.service.SendBotMessageService;

/**
* No {@link Command}.
*/
public class NoCommand implements Command {

   private final SendBotMessageService sendBotMessageService;

   public static final String NO_MESSAGE = "Я поддерживаю команды, начинающиеся со слеша(/).\n"
           + "Для просмотра списка команд введите '/help'";

   public NoCommand(SendBotMessageService sendBotMessageService) {
       this.sendBotMessageService = sendBotMessageService;
   }

   @Override
   public void execute(Update update) {
       sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), NO_MESSAGE);
   }

    @Override
    public void unExecute() {

    }
}