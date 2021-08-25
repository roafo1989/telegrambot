package ru.roafo.telegrambot.command;

import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * Utils class for Commands.
 */
public class CommandUtils {

    /**
     * Retrieve chatId from {@link Update} object.
     *
     * @param update provided {@link Update}
     * @return chatID from the provided {@link Update} object.
     */
    public static String getChatId(Update update) {
        return update.getMessage().getChatId().toString();
    }

    /**
     * Retrieve text of the message from {@link Update} object.
     *
     * @param update provided {@link Update}
     * @return the text of the message from the provided {@link Update} object.
     */
    public static String getMessage(Update update) {
        return update.getMessage().getText().trim();
    }

    public static List<String> getStartMessages() {
        List<String> startMessages = new ArrayList<>();
        startMessages.add("привет");
        startMessages.add("ты кто");
        startMessages.add("?");
        startMessages.add("здравствуй");
        return startMessages;
    }

    public static boolean isStartCommand(String message) {
        for (String text : getStartMessages()) {
            if (message.contains(text)) return true;
        }
        return false;
    }

    public static boolean hasTextMessage(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }


    public static String getUserName(Update update) {
        return !StringUtils.isEmpty(update.getMessage().getFrom().getUserName()) ?
                update.getMessage().getFrom().getUserName() : update.getMessage().getFrom().getFirstName();
    }
}