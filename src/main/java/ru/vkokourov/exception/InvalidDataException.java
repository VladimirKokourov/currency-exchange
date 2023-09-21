package ru.vkokourov.exception;

import lombok.Getter;

import javax.servlet.http.HttpServletResponse;

@Getter
public class InvalidDataException extends ApplicationException {

    public InvalidDataException(String message) {
        super(message);
        statusCode = HttpServletResponse.SC_BAD_REQUEST;
    }
}
