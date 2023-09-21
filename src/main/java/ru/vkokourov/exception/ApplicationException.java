package ru.vkokourov.exception;

import lombok.Getter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

@Getter
public class ApplicationException extends ServletException {

    protected int statusCode;

    public ApplicationException(String message) {
        super(message);
        statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }
}
