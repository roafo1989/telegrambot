package ru.roafo.telegrambot.command;

import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.roafo.telegrambot.domain.TelegramUser;
import ru.roafo.telegrambot.service.SendBotMessageService;
import ru.roafo.telegrambot.service.TelegramUserService;

import java.util.Optional;

public class StartCommand implements Command {

    private SendBotMessageService sendBotMessageService;
    private TelegramUserService telegramUserService;

    public StartCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    public static String START_MESSAGE = "Привет, %s!\n" +
            "Меня зовут Jarvis, и я помогу тебе быть в курсе последних " +
            "статей тех авторов, котрые тебе интересны.\n" +
            "Давай начнём?\n" +
            "Введи /help, чтобы узнать, что я могу";


    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();

        String userName = !StringUtils.isEmpty(update.getMessage().getFrom().getUserName()) ?
                update.getMessage().getFrom().getUserName() : update.getMessage().getFrom().getFirstName();

        Optional<TelegramUser> tgUser = telegramUserService.findByChatId(chatId);
        TelegramUser telegramUser;
        if (tgUser.isPresent()) {
            telegramUser= tgUser.get();
        } else {
            telegramUser = new TelegramUser();
            telegramUser.setChatId(chatId);
        }
        telegramUser.setActive(true);
        telegramUserService.save(telegramUser);

        sendBotMessageService.sendMessage(chatId, String.format(START_MESSAGE, userName));
    }

    @Override
    public void unExecute() {

    }
}
