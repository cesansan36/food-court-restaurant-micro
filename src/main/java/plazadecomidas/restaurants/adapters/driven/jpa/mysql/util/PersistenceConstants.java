package plazadecomidas.restaurants.adapters.driven.jpa.mysql.util;

public class PersistenceConstants {

    private PersistenceConstants() {throw new IllegalStateException("Utility class");}

    public static final String RESTAURANT_ALREADY_EXISTS_MESSAGE = "The restaurant you are trying to add already exists in the database.";
    public static final String MEAL_ALREADY_EXISTS_MESSAGE = "The meal you are trying to add already exists in the database.";
    public static final String MEAL_NOT_FOUND_MESSAGE = "The meal you are trying to update does not exist in the database.";
}
