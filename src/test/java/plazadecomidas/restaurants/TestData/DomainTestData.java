package plazadecomidas.restaurants.TestData;

import plazadecomidas.restaurants.domain.model.Restaurant;

public class DomainTestData {

    private DomainTestData() {
        throw new IllegalStateException("Utility class");
    }

    public static String NAME_FIELD = "name%s";
    public static String ADDRESS_FIELD = "address %s";
    public static String PHONE_NUMBER_FIELD = "+123456%s";
    public static String LOGO_URL_FIELD = "logoUrl %s";
    public static String NIT_FIELD = "123456%s";

    public static Restaurant getValidRestaurant(Long id, Long idOwner) {
        return new Restaurant(1L, NAME_FIELD.formatted(id), ADDRESS_FIELD.formatted(id), idOwner, PHONE_NUMBER_FIELD.formatted(id), LOGO_URL_FIELD.formatted(id), NIT_FIELD.formatted(id));
    }
}
