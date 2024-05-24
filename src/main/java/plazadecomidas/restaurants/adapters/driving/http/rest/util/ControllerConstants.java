package plazadecomidas.restaurants.adapters.driving.http.rest.util;

public class ControllerConstants {

    private ControllerConstants() {
        throw new IllegalStateException("Utility class");
    }

    public enum OrderStatus {
        PENDING,
        PREPARING,
        READY,
        DELIVERED,
        CANCELED
    }

    public static final String OWNER_ID_MISMATCH_MESSAGE = "The id of the owner sent doesn't match the id of the person using the module";
    public static final String USER_CLAIM = "user";
}
