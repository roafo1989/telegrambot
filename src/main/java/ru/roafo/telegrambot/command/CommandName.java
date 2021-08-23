package ru.roafo.telegrambot.command;

public enum CommandName {

    START("/start"),
    STOP("/stop"),
    HELP("/help"),
    NO("/"),
    STAT("/stat"),
    ADD_GROUP_SUB("/addGroupSub"),
    ADD_GROUP_SUB_RUS("/подписаться"),
    LIST_GROUP_SUB("/listGroupSub"),
    ADMIN_HELP("/ahelp")
    ;

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
