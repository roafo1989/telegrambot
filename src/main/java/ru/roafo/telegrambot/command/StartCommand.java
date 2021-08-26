package ru.roafo.telegrambot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.roafo.telegrambot.domain.TelegramUser;
import ru.roafo.telegrambot.service.SendBotMessageService;
import ru.roafo.telegrambot.service.TelegramUserService;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class StartCommand implements Command {

    @Value("${message.start}")
    private String startMessage;

    private SendBotMessageService sendBotMessageService;

    private TelegramUserService telegramUserService;

    private StartCommand startCommand;

    @Autowired
    public void setStartCommand(StartCommand startCommand) {
        this.startCommand = startCommand;
    }

    @Autowired
    public StartCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        String chatId = CommandUtils.getChatId(update);
        setActiveUser(chatId);
        String startText = generateStartText(update);
        sendBotMessageService.sendMessage(chatId, startText);
    }

    private void setActiveUser(String chatId) {
        TelegramUser telegramUser = startCommand.getTelegramUser(chatId);
        telegramUser.setActive(true);
        telegramUserService.save(telegramUser);
    }

    protected TelegramUser getTelegramUser(String chatId) {
        Optional<TelegramUser> userOptional = telegramUserService.findByChatId(chatId);
        TelegramUser telegramUser;
        if (userOptional.isPresent()) {
            telegramUser = userOptional.get();
        } else {
            telegramUser = new TelegramUser();
            telegramUser.setChatId(chatId);
        }
        return telegramUser;
    }


    private String generateStartText(Update update) {
        String userName = startCommand.getUserName(update);
        return String.format(new String(startMessage.getBytes(StandardCharsets.ISO_8859_1)), userName);
    }

    protected String getUserName(Update update) {
        return !StringUtils.isEmpty(update.getMessage().getFrom().getUserName()) ?
                update.getMessage().getFrom().getUserName() : update.getMessage().getFrom().getFirstName();
    }

    @Override
    public void unExecute() {

    }
}
