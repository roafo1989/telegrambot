package ru.roafo.telegrambot.command;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.roafo.telegrambot.domain.GroupSub;
import ru.roafo.telegrambot.domain.TelegramUser;
import ru.roafo.telegrambot.service.SendBotMessageService;
import ru.roafo.telegrambot.service.TelegramUserService;

import javax.ws.rs.NotFoundException;
import java.util.stream.Collectors;

import static ru.roafo.telegrambot.command.CommandUtils.getChatId;


/**
* {@link Command} for getting list of {@link GroupSub}.
*/
@Component
public class ListGroupSubCommand implements Command {

   private final SendBotMessageService sendBotMessageService;
   private final TelegramUserService telegramUserService;

   @Autowired
   public ListGroupSubCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
       this.sendBotMessageService = sendBotMessageService;
       this.telegramUserService = telegramUserService;
   }

   @Override
   public void execute(Update update) {
       //todo add exception handling
       TelegramUser telegramUser = telegramUserService.findByChatId(getChatId(update)).orElseThrow(NotFoundException::new);

       String message = "Я нашел все подписки на группы: \n\n";
       String collectedGroups = telegramUser.getGroupSubs().stream()
               .map(it -> "Группа: " + it.getTitle() + " , ID = " + it.getId() + " \n")
               .collect(Collectors.joining());

       sendBotMessageService.sendMessage(telegramUser.getChatId(), message + collectedGroups);
   }

    @Override
    public void unExecute() {

    }
}