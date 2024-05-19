package plazadecomidas.restaurants.domain.util;

public class DomainConstants {

    private DomainConstants() {
        throw new IllegalStateException("Utility class");
    }

    public enum Fields {
        NAME,
        ADDRESS,
        OWNER_ID,
        PHONE_NUMBER,
        LOGO_URL,
        NIT
    }

    public static final String EMPTY_FIELD_MESSAGE = "The field %s cannot be empty";
    public static final String PHONE_NUMBER_CHAR_AMOUNT_EXCEEDED = "The cell phone number can not have more than %s characters";
    public static final String NAME_INVALID_MESSAGE = "The name can only contain alphanumeric characters and must have at least 1 alphabetic character";
    public static final String NIT_INVALID_MESSAGE = "The nit can only contain numeric characters";
}
