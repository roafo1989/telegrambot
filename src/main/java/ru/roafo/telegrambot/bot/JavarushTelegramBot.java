package ru.roafo.telegrambot.bot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.roafo.telegrambot.command.CommandContainer;
import ru.roafo.telegrambot.javarushclient.service.GroupSubService;
import ru.roafo.telegrambot.javarushclient.service.JavaRushGroupClient;
import ru.roafo.telegrambot.service.SendBotMessageServiceImpl;
import ru.roafo.telegrambot.service.TelegramUserService;

import java.util.List;

import static ru.roafo.telegrambot.command.CommandName.NO;
import static ru.roafo.telegrambot.command.CommandName.START;


@Component
public class JavarushTelegramBot extends TelegramLongPollingBot {

    public static String COMMAND_PREFIX = "/";

    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    private final CommandContainer commandContainer;

    @Autowired
    public JavarushTelegramBot(TelegramUserService telegramUserService,
                               JavaRushGroupClient groupClient,
                               GroupSubService groupSubService,
                               @Value("#{'${bot.admins}'.split(',')}") List<String> admins) {
        this.commandContainer = new CommandContainer(
                new SendBotMessageServiceImpl(this),
                telegramUserService,
                groupClient,
                groupSubService,
                admins);
    }

    /**
     * Метод возвращает token бота для связи с сервером Telegram
     * @return token для бота
     */
    @Override
    public String getBotToken() {
        return token;
    }


    /**
     * Метод для приема сообщений.
     * @param update Содержит сообщение от пользователя.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            String chatId = update.getMessage().getChatId().toString();
            if (message.startsWith(COMMAND_PREFIX)) {
                String commandIdentifier = message.split(" ")[0];

                commandContainer.retrieveCommand(commandIdentifier).execute(update);
            } else if(message.toLowerCase().contains("привет") || (message.toLowerCase().contains("ты кто")) || message.contains("?")) {
                commandContainer.retrieveCommand(START.getCommandName()).execute(update);
            } else {
                commandContainer.retrieveCommand(NO.getCommandName()).execute(update);
            }

            SendMessage sm = new SendMessage();
            sm.setChatId(chatId);
            sm.setText(message);

            try {
                execute(sm);
            } catch (TelegramApiException e) {
                //todo add logging to the project.
                e.printStackTrace();
            }
        }
    }

    /**
     * Метод возвращает имя бота, указанное при регистрации.
     * @return имя бота
     */
    @Override
    public String getBotUsername() {
        return username;
    }

}
