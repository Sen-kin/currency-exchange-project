package utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class ValidationUtil {
    private static final Integer ISO_CODE_LENGTH = 3;
    private static final Integer MAX_NAME_LENGTH = 25;
    private static final Integer MAX_SIGN_LENGTH = 4;
    private static final Integer MAX_NUMBER_LENGTH = 15;

    public static void codeValidation(String code) {
        if (StringUtils.isEmpty(code)) {
            throw new IllegalArgumentException("Code cannot be empty");
        }
        if (!StringUtils.isAlpha(code) || !StringUtils.isAllUpperCase(code)) {
            throw new IllegalArgumentException("Code must be ENGLISH UPPER");
        }
        if (code.length() != ISO_CODE_LENGTH) {
            throw new IllegalArgumentException("Code doesn't meet the ISO 4217");
        }
    }

    public static void nameAndSignValidation(String name, String sign) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(sign)) {
            throw new IllegalArgumentException("Name and sign be empty");
        }
        if (!StringUtils.isAlphaSpace(name)) {
            throw new IllegalArgumentException("Name must be Alphabetic");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("Currency's name is too big");
        }
        if (sign.length() > MAX_SIGN_LENGTH) {
            throw new IllegalArgumentException("Currency's sign is too big");
        }
    }

    public static void pathCurrencyValidation(String path) {
        if (path == null || path.length() != 4) {
            throw new IllegalArgumentException("Please, enter the Currency code");
        }
        String code = path.substring(1);
        codeValidation(code);
    }

    public static void exchangeRateCodesValidation(String baseCurrencyCode, String targetCurrencyCode) {
        if (StringUtils.isEmpty(baseCurrencyCode) || StringUtils.isEmpty(targetCurrencyCode)) {
            throw new IllegalArgumentException("Fields cannot be empty");
        }
        if (baseCurrencyCode.length() != ISO_CODE_LENGTH || targetCurrencyCode.length() != ISO_CODE_LENGTH || !StringUtils.isAllUpperCase(baseCurrencyCode + targetCurrencyCode)) {
            throw new IllegalArgumentException("Code(s) Is (are) don't (doesn't) meet ISO 4217");
        }
    }

    public static void pathExchangeRateValidation(String path) {
        if (StringUtils.isEmpty(path) || StringUtils.containsWhitespace(path) || path.length() != 7) {
            throw new IllegalArgumentException("Missing currency codes pair");
        }
        String baseCurrencyCode = path.substring(1, 4);
        String targetCurrencyCode = path.substring(4, 7);
        exchangeRateCodesValidation(baseCurrencyCode, targetCurrencyCode);
    }

    public static void numberValidation(String number) {
        if (StringUtils.isEmpty(number)) {
            throw new IllegalArgumentException("Field cannot be empty");
        }
        if (number.length() > MAX_NUMBER_LENGTH) {
            throw new IllegalArgumentException("It's too big");
        }
        try {
            Double.parseDouble(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Incorrect number format");
        }
    }
}
