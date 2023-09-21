package ru.vkokourov.exception;

import javax.servlet.ServletException;

public class ServerException extends ApplicationException {

    public ServerException(String message) {
        super(message);
    }
}
