package ru.vkokourov.exception;

import javax.servlet.http.HttpServletResponse;

public class NotExistException extends ApplicationException {

    public NotExistException(String message) {
        super(message);
        statusCode = HttpServletResponse.SC_NOT_FOUND;
    }
}
