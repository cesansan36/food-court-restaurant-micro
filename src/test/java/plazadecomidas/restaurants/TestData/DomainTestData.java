package plazadecomidas.restaurants.TestData;

import plazadecomidas.restaurants.domain.model.Category;
import plazadecomidas.restaurants.domain.model.Meal;
import plazadecomidas.restaurants.domain.model.MealOrder;
import plazadecomidas.restaurants.domain.model.Order;
import plazadecomidas.restaurants.domain.model.OrderRecord;
import plazadecomidas.restaurants.domain.model.Restaurant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DomainTestData {

    private DomainTestData() {
        throw new IllegalStateException("Utility class");
    }

    public static String NAME_FIELD = "name%s";
    public static String DESCRIPTION_FIELD = "description%s";
    public static String ADDRESS_FIELD = "address %s";
    public static String PHONE_NUMBER_FIELD = "+123456%s";
    public static String IMG_URL_FIELD = "logoUrl %s";
    public static String NIT_FIELD = "123456%s";
    public static Long PRICE = 100L;
    public static String STATUS = "PENDING";

    public static Restaurant getValidRestaurant(Long id, Long idOwner) {
        return new Restaurant(1L, NAME_FIELD.formatted(id), ADDRESS_FIELD.formatted(id), idOwner, PHONE_NUMBER_FIELD.formatted(id), IMG_URL_FIELD.formatted(id), NIT_FIELD.formatted(id));
    }

    public static Category getValidCategory(Long id) {
        return new Category(1L, NAME_FIELD.formatted(id), DESCRIPTION_FIELD.formatted(id));
    }

    public static Meal getValidMeal(Long id) {
        return new Meal(1L, NAME_FIELD.formatted(id), DESCRIPTION_FIELD.formatted(id), PRICE+id, IMG_URL_FIELD.formatted(id), true, getValidRestaurant(id, 1L), getValidCategory(id));
    }

    public static MealOrder getValidMealOrder(Long idMeal) {
        return new MealOrder(idMeal, null, 1);
    }

    public static Order getValidOrder (Long id) {
        return new Order(id, id, LocalDate.now(), "PENDING", 1111, id, id, List.of(getValidMealOrder(id)));
    }

    public static OrderRecord getValidOrderRecord (Long id) {
        return new OrderRecord(
                id.toString(),
                id,
                id,
                "client%s@email.com".formatted(id),
                LocalDateTime.now(),
                "PENDING",
                "PREPARING",
                id,
                "employee%s@somemail.com".formatted(id));
    }
}
