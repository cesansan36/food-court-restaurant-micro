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

    public static final String USER_CLAIM = "user";
}
