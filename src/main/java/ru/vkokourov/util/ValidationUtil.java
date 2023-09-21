package ru.vkokourov.util;

public class ValidationUtil {

    public static boolean isCodeValid(String code) {
        return code.matches("[A-Z]{3}");
    }
}
