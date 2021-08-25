package ru.roafo.telegrambot.bot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.roafo.telegrambot.command.Command;
import ru.roafo.telegrambot.command.CommandContainer;
import ru.roafo.telegrambot.command.CommandUtils;

@Component
public class JavarushTelegramBot extends TelegramLongPollingBot {

    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    public CommandContainer commandContainer;

    @Autowired
    public void setCommandContainer(CommandContainer commandContainer) {
        this.commandContainer = commandContainer;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    /**
     * Метод для приема сообщений.
     *
     * @param update Содержит сообщение от пользователя.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (hasMessageText(update)) {
            Command command = this.commandContainer.retrieveCommand(update);
            command.execute(update);
            SendMessage sm = getSendMessage(update);
            try {
                execute(sm);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private SendMessage getSendMessage(Update update) {
        SendMessage sm = new SendMessage();
        sm.setChatId(CommandUtils.getChatId(update));
        sm.setText(CommandUtils.getMessage(update));
        return sm;
    }

    private boolean hasMessageText(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }


}
