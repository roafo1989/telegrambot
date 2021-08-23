package ru.roafo.telegrambot.javarushclient.service;


import ru.roafo.telegrambot.domain.GroupSub;
import ru.roafo.telegrambot.javarushclient.dto.GroupDiscussionInfo;

import java.util.List;

/**
* Service for manipulating with {@link GroupSub}.
*/
public interface GroupSubService {

   GroupSub save(String chatId, GroupDiscussionInfo groupDiscussionInfo);

   GroupSub save(GroupSub groupSub);

   List<GroupSub> findAll();
}