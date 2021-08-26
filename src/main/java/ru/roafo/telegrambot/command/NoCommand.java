package ru.roafo.telegrambot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.roafo.telegrambot.service.SendBotMessageService;

import java.nio.charset.StandardCharsets;

/**
* No {@link Command}.
*/
@Component
public class NoCommand implements Command {

   private final SendBotMessageService sendBotMessageService;

   @Value("${message.noMessage}")
   public String noMessage;

   @Autowired
   public NoCommand(SendBotMessageService sendBotMessageService) {
       this.sendBotMessageService = sendBotMessageService;
   }

   @Override
   public void execute(Update update) {
       sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), new String(noMessage.getBytes(StandardCharsets.ISO_8859_1)));
   }

    @Override
    public void unExecute() {

    }
}