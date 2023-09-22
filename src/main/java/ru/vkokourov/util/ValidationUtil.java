package ru.vkokourov.util;

import java.util.Map;

public class ValidationUtil {

    private static final String REGEX_CODE_VALID = "[A-Z]{3}";
    private static final int AMOUNT_PARAMETERS_FOR_CURRENCY = 3;

    public static boolean isCodeValid(String code) {
        return code.matches(REGEX_CODE_VALID);
    }

    public static boolean isParameterForCurrencyMissing(Map<String, String[]> parameterMap) {
        if (parameterMap.keySet().size() != AMOUNT_PARAMETERS_FOR_CURRENCY) {
            return true;
        }
        if (!parameterMap.containsKey("code") ||
                !parameterMap.containsKey("name") ||
                !parameterMap.containsKey("sign")) {
            return true;
        }
        return false;
    }

    public static boolean isParameterMapForCurrencyValid(Map<String, String[]> parameterMap) {
        String code = parameterMap.get("code")[0];
        if (!isCodeValid(code)) {
            return false;
        }
        String sign = parameterMap.get("sign")[0];
        if (sign.length() != 1) {
            return false;
        }
        return true;
    }
}
