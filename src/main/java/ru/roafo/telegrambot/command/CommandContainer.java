package ru.roafo.telegrambot.command;


import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.roafo.telegrambot.command.annotation.AdminCommand;
import ru.roafo.telegrambot.service.SendBotMessageService;

import java.util.List;

import static java.util.Objects.nonNull;
import static ru.roafo.telegrambot.command.CommandName.*;

@Component
public class CommandContainer {

    public static String COMMAND_PREFIX = "/";

    private ImmutableMap<String, Command> commandMap;

    private Command unknownCommand;

    @Value("#{'${bot.admins}'.split(',')}")
    private List<String> admins;

    public CommandContainer(SendBotMessageService sendBotMessageService,
                            StartCommand startCommand,
                            StopCommand stopCommand,
                            HelpCommand helpCommand,
                            NoCommand noCommand,
                            StatCommand statCommand,
                            AddGroupSubCommand addGroupSubCommand,
                            ListGroupSubCommand listGroupSubCommand,
                            AdminHelpCommand adminHelpCommand) {
        commandMap = ImmutableMap.<String, Command>builder()
                .put(START.getCommandName(), startCommand)
                .put(STOP.getCommandName(), stopCommand)
                .put(HELP.getCommandName(), helpCommand)
                .put(NO.getCommandName(), noCommand)
                .put(STAT.getCommandName(), statCommand)
                .put(ADD_GROUP_SUB.getCommandName(), addGroupSubCommand)
                .put(ADD_GROUP_SUB_RUS.getCommandName(), addGroupSubCommand)
                .put(LIST_GROUP_SUB.getCommandName(), listGroupSubCommand)
                .put(ADMIN_HELP.getCommandName(), adminHelpCommand)
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public Command retrieveCommand(Update update) {
        String message = CommandUtils.getMessage(update);
        if (message.startsWith(COMMAND_PREFIX)) {
            String commandIdentifier = message.split(" ")[0];

            return commandMap.getOrDefault(commandIdentifier, unknownCommand);
        } else if (message.toLowerCase().contains("привет") || (message.toLowerCase().contains("ты кто")) || message.contains("?")) {
            return commandMap.getOrDefault(START.getCommandName(), unknownCommand);
        } else {
            return commandMap.getOrDefault(NO.getCommandName(), unknownCommand);
        }
    }

    public Command retrieveCommand(String commandIdentifier, String username) {
        Command orDefault = commandMap.getOrDefault(commandIdentifier, unknownCommand);
        if (isAdminCommand(orDefault)) {
            if (admins.contains(username)) {
                return orDefault;
            } else {
                return unknownCommand;
            }
        }
        return orDefault;
    }

    private boolean isAdminCommand(Command command) {
        return nonNull(command.getClass().getAnnotation(AdminCommand.class));
    }
}
