package ru.roafo.telegrambot.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.roafo.telegrambot.domain.GroupSub;
import ru.roafo.telegrambot.domain.TelegramUser;
import ru.roafo.telegrambot.javarushclient.dto.PostInfo;
import ru.roafo.telegrambot.javarushclient.service.GroupSubService;
import ru.roafo.telegrambot.javarushclient.service.JavaRushPostClient;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindNewArticleServiceImpl implements FindNewArticleService {

   public static final String JAVARUSH_WEB_POST_FORMAT = "https://javarush.ru/groups/posts/%s";

   private final GroupSubService groupSubService;
   private final JavaRushPostClient javaRushPostClient;
   private final SendBotMessageService sendMessageService;

   @Autowired
   public FindNewArticleServiceImpl(GroupSubService groupSubService,
                                    JavaRushPostClient javaRushPostClient,
                                    SendBotMessageService sendMessageService) {
       this.groupSubService = groupSubService;
       this.javaRushPostClient = javaRushPostClient;
       this.sendMessageService = sendMessageService;
   }


   @Override
   public void findNewArticles() {
       groupSubService.findAll().forEach(gSub -> {
           List<PostInfo> newPosts = javaRushPostClient.findNewPosts(gSub.getId(), gSub.getLastArticleId());
           setNewLastArticleId(gSub, newPosts);
           notifySubscribersAboutNewArticles(gSub, newPosts);
       });
   }

    private void setNewLastArticleId(GroupSub gSub, List<PostInfo> newPosts) {
        newPosts.stream().mapToInt(PostInfo::getId).max()
                .ifPresent(id -> {
                    gSub.setLastArticleId(id);
                    groupSubService.save(gSub);
                });
    }

   private void notifySubscribersAboutNewArticles(GroupSub gSub, List<PostInfo> newPosts) {
       Collections.reverse(newPosts);
       List<String> messagesWithNewArticles = newPosts.stream()
               .map(post -> String.format("????????????? ?????????? ???????????? <b>%s</b> ?? ???????????? <b>%s</b>.???\n\n" +
                               "<b>????????????????:</b> %s\n\n" +
                               "<b>????????????:</b> %s\n",
                       post.getTitle(),
                       gSub.getTitle(),
                       post.getDescription(),
                       getPostUrl(post.getKey())))
               .collect(Collectors.toList());

       gSub.getUsers().stream()
               .filter(TelegramUser::isActive)
               .forEach(it -> sendMessageService.sendMessage(it.getChatId(), messagesWithNewArticles));
   }

   private String getPostUrl(String key) {
       return String.format(JAVARUSH_WEB_POST_FORMAT, key);
   }
}