package ru.roafo.telegrambot.command;


import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.roafo.telegrambot.service.SendBotMessageService;

import java.nio.charset.StandardCharsets;

/**
* Unknown {@link Command}.
*/
public class UnknownCommand implements Command {

   @Value("${message.unknown}")
   public String unknownMessage;

   private final SendBotMessageService sendBotMessageService;

   public UnknownCommand(SendBotMessageService sendBotMessageService) {
       this.sendBotMessageService = sendBotMessageService;
   }

   @Override
   public void execute(Update update) {
       sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), new String(unknownMessage.getBytes(StandardCharsets.ISO_8859_1)));
   }

    @Override
    public void unExecute() {

    }
}