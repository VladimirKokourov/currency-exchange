package ru.vkokourov.exception;

import javax.servlet.http.HttpServletResponse;

public class InvalidDataException extends ApplicationException {

    public InvalidDataException(String message) {
        super(message);
        statusCode = HttpServletResponse.SC_BAD_REQUEST;
    }
}
