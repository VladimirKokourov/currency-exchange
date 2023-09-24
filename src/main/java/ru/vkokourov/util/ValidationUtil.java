package ru.vkokourov.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class ValidationUtil {

    private static final String REGEX_CODE_VALID = "[A-Z]{3}";
    private static final String REGEX_ID_VALID = "[0-9]+";
    private static final String PARAMETER_ID = "id";
    private static final String PARAMETER_CODE = "code";
    private static final String PARAMETER_NAME = "name";
    private static final String PARAMETER_SIGN = "sign";
    private static final int AMOUNT_PARAMETERS_FOR_CURRENCY = 4;

    public static boolean isCodeValid(String code) {
        return code.matches(REGEX_CODE_VALID);
    }

    public static boolean isParameterForCurrencyMissing(Map<String, String[]> parameterMap) {
        log.info("Check amount parameters {}. Should be {} counting id", parameterMap.size(), AMOUNT_PARAMETERS_FOR_CURRENCY);
        if (parameterMap.containsKey(PARAMETER_ID) &&
                parameterMap.size() != AMOUNT_PARAMETERS_FOR_CURRENCY) {
            return true;
        } else if (!parameterMap.containsKey(PARAMETER_ID) &&
                parameterMap.size() != AMOUNT_PARAMETERS_FOR_CURRENCY - 1) {
            return true;
        }
        if (!parameterMap.containsKey(PARAMETER_CODE) ||
                !parameterMap.containsKey(PARAMETER_NAME) ||
                !parameterMap.containsKey(PARAMETER_SIGN)) {
            return true;
        }

        return false;
    }

    public static boolean isParameterMapForCurrencyValid(Map<String, String[]> parameterMap) {
        String id = parameterMap.getOrDefault("id", new String[]{"0"})[0];
        if (!id.matches(REGEX_ID_VALID)) {
            return false;
        }

        String code = parameterMap.get(PARAMETER_CODE)[0];
        if (!isCodeValid(code)) {
            return false;
        }
        String sign = parameterMap.get(PARAMETER_SIGN)[0];
        if (sign.length() != 1) {
            return false;
        }

        return true;
    }
}
