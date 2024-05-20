package plazadecomidas.restaurants.domain.util;

import java.util.regex.Pattern;

public class Validator {

    private Validator() { throw new IllegalStateException("Utility class"); }

    private static final String PHONE_NUMBER_PATTERN = "^(\\+)?\\d{1,13}$";
    private static final String NUMBERS_ONLY_PATTERN = "\\d+";
    private static final String ALPHANUMERIC_WITH_AT_LEAST_ONE_LETTER_PATTERN = "^\\d*[a-zA-Z][a-zA-Z0-9]*$";

    private static final Pattern PHONE_NUMBER_REGEX = Pattern.compile(PHONE_NUMBER_PATTERN);
    private static final Pattern NUMBERS_ONLY_REGEX = Pattern.compile(NUMBERS_ONLY_PATTERN);
    private static final Pattern ALPHANUMERIC_WITH_AT_LEAST_ONE_LETTER_REGEX = Pattern.compile(ALPHANUMERIC_WITH_AT_LEAST_ONE_LETTER_PATTERN);

    public static boolean isFieldEmpty(String field) {
        return field == null || field.trim().isEmpty();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return PHONE_NUMBER_REGEX.matcher(phoneNumber).matches();
    }

    public static boolean isValidNit(String nit) {
        return NUMBERS_ONLY_REGEX.matcher(nit).matches();
    }

    public static boolean isValidName(String name) {
        return ALPHANUMERIC_WITH_AT_LEAST_ONE_LETTER_REGEX.matcher(name).matches();
    }

    public static boolean isValidPrice(Long price) {
        return price > 0;
    }
}
