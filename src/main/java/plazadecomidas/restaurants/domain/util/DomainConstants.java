package plazadecomidas.restaurants.domain.util;

public class DomainConstants {



    private DomainConstants() {
        throw new IllegalStateException("Utility class");
    }

    public enum RestaurantFields {
        NAME,
        ADDRESS,
        OWNER_ID,
        PHONE_NUMBER,
        LOGO_URL,
        NIT
    }
    public enum MealFields {
        NAME,
        DESCRIPTION,
        PRICE,
        IMAGE_URL,
        IS_ACTIVE,
        RESTAURANT,
        CATEGORY
    }

    public enum OrderFields {
        ID_CLIENT,
        DATE,
        STATUS,
        ID_CHEF,
        ID_RESTAURANT,
        MEALS
    }

    public enum OrderStatus {
        PENDING,
        PREPARING,
        READY,
        DELIVERED,
        CANCELLED;

        public static boolean isValidStatus(String status) {
            for (OrderStatus orderStatus : OrderStatus.values()) {
                if (orderStatus.name().equals(status)) {
                    return true;
                }
            }
            return false;
        }
    }

    public enum MealOrderFields {
        ID_MEAL,
        ID_ORDER,
        QUANTITY
    }

    public static final String OWNER_ROLE = "ROLE_OWNER";
    public static final Integer PHONE_NUMBER_MAX_LENGTH = 13;

    public static final String CATEGORY_NOT_FOUND_MESSAGE = "The category doesn't exists";
    public static final String EMPTY_FIELD_MESSAGE = "The field %s cannot be empty";
    public static final String PHONE_NUMBER_NOT_VALID_MESSAGE = "The cell phone number can not have more than %s characters, can only contain a + sign at the start";
    public static final String NAME_INVALID_MESSAGE = "The name can only contain alphanumeric characters and must have at least 1 alphabetic character";
    public static final String NIT_INVALID_MESSAGE = "The nit can only contain numeric characters";
    public static final String OWNER_ID_INVALID_MESSAGE = "The Id of the user doesn't have the required role";
    public static final String USER_ID_NOT_FOUND_MESSAGE = "Could not find the user - Response obtained was -> %s";
    public static final String PRICE_NOT_VALID = "The price must be a whole number greater than 0";
    public static final String RESTAURANT_OWNER_MISMATCH_MESSAGE = "The restaurant doesn't belong to the user";
    public static final String CLIENT_HAS_UNFINISHED_ORDERS_MESSAGE = "Client has unfinished orders";
    public static final String WRONG_STATUS_MESSAGE = "The status used is not a valid status";
    public static final String QUANTITY_NEGATIVE_MESSAGE = "The quantity must be greater than 0";
    public static final String ORDER_CANCELLED_SUCCESS_MESSAGE = "The order was cancelled successfully";
    public static final String ORDER_CANCELLED_FAILED_MESSAGE = "Lo sentimos, tu pedido ya está en preparación y no puede cancelarse";
}
