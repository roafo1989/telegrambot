package ru.roafo.telegrambot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.roafo.telegrambot.service.SendBotMessageService;
import ru.roafo.telegrambot.service.TelegramUserService;

/**
* Stop {@link Command}.
*/
public class StopCommand implements Command {

   private final SendBotMessageService sendBotMessageService;
    private TelegramUserService telegramUserService;

   public static final String STOP_MESSAGE = "Деактивировал все ваши подписки \uD83D\uDE1F.";

   public StopCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
       this.sendBotMessageService = sendBotMessageService;
       this.telegramUserService = telegramUserService;
   }

   @Override
   public void execute(Update update) {
       String chatId = update.getMessage().getChatId().toString();
       sendBotMessageService.sendMessage(chatId, STOP_MESSAGE);

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