package ru.vkokourov.exception;

import javax.servlet.ServletException;

public class ServerException extends ServletException {

    private String message;

    public ServerException() {
        super("Ошибка сервера");
    }

    public ServerException(String message) {
        super(message);
    }
}
