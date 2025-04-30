package util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class ValidationUtil {
    private static final Integer CODE_LENGTH = 3;
    private static final Integer MAX_NAME_LENGTH = 25;
    private static final Integer MAX_SIGN_LENGTH = 4;
    private static final Integer MAX_NUMBER_LENGTH = 15;

    public static void currencyValidation(String code, String name, String sign) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(code) || StringUtils.isEmpty(sign)) {
            throw new IllegalArgumentException("Fields cannot be empty");
        }

        if (!StringUtils.isAlphaSpace(name) || !StringUtils.isAlpha(code)) {
            throw new IllegalArgumentException("Name and Code must be Alphabetic");
        }

        if (code.length() != CODE_LENGTH || !StringUtils.isAllUpperCase(code)) {
            throw new IllegalArgumentException("Code Is Incorrect! Go and read ISO 4217");
        }

        if (name.length() > MAX_NAME_LENGTH) throw new IllegalArgumentException("Currency's name is too big");

        if (sign.length() > MAX_SIGN_LENGTH) throw new IllegalArgumentException("Currency's sign is too big");
    }

    public static void pathCurrencyValidation(String path) {
        if (path == null || path.length() != 4) throw new IllegalArgumentException("Please, enter the Currency code");

        String code = path.substring(1);

        if (!StringUtils.isAllUpperCase(code) || !StringUtils.isAlpha(code) || code.length() != CODE_LENGTH) {
            throw new IllegalArgumentException("Currency is 3 UPPER English Letters");
        }
    }

    public static void exchangeCodesValidation(String baseCurrencyCode, String targetCurrencyCode) {
        if (StringUtils.isEmpty(baseCurrencyCode) || StringUtils.isEmpty(targetCurrencyCode)) {
            throw new IllegalArgumentException("Fields cannot be empty");
        }
        if (baseCurrencyCode.length() != CODE_LENGTH || targetCurrencyCode.length() != CODE_LENGTH || !StringUtils.isAllUpperCase(baseCurrencyCode + targetCurrencyCode)) {
            throw new IllegalArgumentException("Code(s) Is (are) Incorrect! Go and read ISO 4217");
        }
    }

    public static void pathExchangeRateValidation(String path) {
        if (StringUtils.isEmpty(path) || StringUtils.containsWhitespace(path) || path.length() != 7) {
            throw new IllegalArgumentException("Missing currency codes pair");
        }
        String pairOfCodes = path.substring(1, 7);

        if (!StringUtils.isAlpha(pairOfCodes) || !StringUtils.isAllUpperCase(pairOfCodes)) {
            throw new IllegalArgumentException("Path must only contains 2 eng. UPPER codes");
        }
    }

    public static void numberValidation(String rate) {
        if (StringUtils.isEmpty(rate)) {
            throw new IllegalArgumentException("Rate(Amount) cannot be empty!");
        }
        if (!StringUtils.isNumeric(rate.replaceAll("[.,]", ""))) {
            throw new IllegalArgumentException("Rate(Amount) must be a number");
        }
        if (StringUtils.countMatches(rate, ".") > 1) throw new IllegalArgumentException("Rate(Amount) contains only 1 dot");

        if (rate.contains(",")) throw new IllegalArgumentException("Please, replace comma with dot");

        if (rate.length() > MAX_NUMBER_LENGTH) throw new IllegalArgumentException("Rate(Amount) is too big :(");

    }
}

