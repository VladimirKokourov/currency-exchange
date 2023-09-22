package ru.vkokourov.exception;

import javax.servlet.http.HttpServletResponse;

public class AlreadyExistException extends ApplicationException {
    public AlreadyExistException(String message) {
        super(message);
        statusCode = HttpServletResponse.SC_CONFLICT;
    }
}
