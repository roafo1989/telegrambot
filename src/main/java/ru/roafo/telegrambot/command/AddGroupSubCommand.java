package ru.roafo.telegrambot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.roafo.telegrambot.domain.GroupSub;
import ru.roafo.telegrambot.javarushclient.dto.GroupDiscussionInfo;
import ru.roafo.telegrambot.javarushclient.dto.GroupRequestArgs;
import ru.roafo.telegrambot.javarushclient.service.GroupSubService;
import ru.roafo.telegrambot.javarushclient.service.JavaRushGroupClient;
import ru.roafo.telegrambot.service.SendBotMessageService;

import java.util.stream.Collectors;


import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import static ru.roafo.telegrambot.command.CommandName.ADD_GROUP_SUB;
import static ru.roafo.telegrambot.command.CommandName.ADD_GROUP_SUB_RUS;
import static ru.roafo.telegrambot.command.CommandUtils.*;

/**
* Add Group subscription {@link Command}.
*/
@Component
public class AddGroupSubCommand implements Command {

   private final SendBotMessageService sendBotMessageService;
   private final JavaRushGroupClient javaRushGroupClient;
   private final GroupSubService groupSubService;

   @Autowired
   public AddGroupSubCommand(SendBotMessageService sendBotMessageService, JavaRushGroupClient javaRushGroupClient,
                             GroupSubService groupSubService) {
       this.sendBotMessageService = sendBotMessageService;
       this.javaRushGroupClient = javaRushGroupClient;
       this.groupSubService = groupSubService;
   }

   @Override
   public void execute(Update update) {
       String messageText = getMessage(update);

       if (messageText.equalsIgnoreCase(ADD_GROUP_SUB.getCommandName()) || messageText.equalsIgnoreCase(ADD_GROUP_SUB_RUS.getCommandName())) {
           sendGroupIdList(getChatId(update));
           return;
       }
       String groupId = messageText.split(SPACE)[1];
       String chatId = getChatId(update);
       if (isNumeric(groupId)) {
           GroupDiscussionInfo groupById = javaRushGroupClient.getGroupById(Integer.parseInt(groupId));
           if (isNull(groupById.getId())) {
               sendGroupNotFound(chatId, groupId);
           }
           GroupSub savedGroupSub = groupSubService.save(chatId, groupById);
           sendBotMessageService.sendMessage(chatId, "Подписал на группу " + savedGroupSub.getTitle());
       } else {
           sendGroupNotFound(chatId, groupId);
       }
   }

   private void sendGroupNotFound(String chatId, String groupId) {
       String groupNotFoundMessage = "Нет группы с ID = \"%s\"";
       sendBotMessageService.sendMessage(chatId, String.format(groupNotFoundMessage, groupId));
   }

   private void sendGroupIdList(String chatId) {
       String groupIds = javaRushGroupClient.getGroupList(GroupRequestArgs.builder().build()).stream()
               .map(group -> String.format("%s - %s \n", group.getTitle(), group.getId()))
               .collect(Collectors.joining());

       String message = "Чтобы подписаться на группу, передай команду вместе с ID группы. \n" +
               "Например: /addGroupSub 16. \n\n" +
               "я подготовил список всех групп - выбирай, какую хочешь :) \n\n" +
               "имя группы - ID группы \n\n" +
               "%s";

       sendBotMessageService.sendMessage(chatId, String.format(message, groupIds));
   }

    @Override
    public void unExecute() {

    }
}