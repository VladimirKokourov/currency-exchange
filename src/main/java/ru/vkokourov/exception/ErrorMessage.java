package ru.vkokourov.exception;

public class ErrorMessage {

    private final String message;

    public ErrorMessage(int statusCode, String errorMessage) {
        message = statusCode + ": " + errorMessage;
    }

    public String getMessage() {
        return message;
    }
}
