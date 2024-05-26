package plazadecomidas.restaurants.adapters.driven.jpa.mysql.util;

public class PersistenceConstants {

    private PersistenceConstants() {throw new IllegalStateException("Utility class");}

    public static final String ALL_STATUS_FILTER = "ALL";

    public static final String RESTAURANT_ALREADY_EXISTS_MESSAGE = "The restaurant you are trying to add already exists in the database.";
    public static final String MEAL_ALREADY_EXISTS_MESSAGE = "The meal you are trying to add already exists in the database.";
    public static final String MEAL_NOT_FOUND_MESSAGE = "The meal you are trying to update does not exist in the database.";
    public static final String ORDER_NOT_FOUND_MESSAGE = "The order you are trying to update does not exist in the database.";
    public static final String RESTAURANT_OWNER_MATCH_NOT_FOUND = "Can't find the restaurant, either it doesn't exist or doesn't belong to the user";
    public static final String MEAL_RESTAURANT_MISMATCH_EXCEPTION = "At least one meal doesn't belong to the selected restaurant";
    public static final String WRONG_STATUS_FILTER_MESSAGE = "The filter you are trying to use is not valid. Please, try again";
    public static final String STATUS_PREPARING_ONLY_FROM_PENDING_MESSAGE = "Can't change status to preparing if the order is not pending";
    public static final String STATUS_READY_ONLY_FROM_PREPARING_MESSAGE = "Can't change status to ready if the order is not preparing";
    public static final String ORDER_EMPLOYEE_MISMATCH_MESSAGE = "The order and the user doesn't belong to the same restaurant";
}
