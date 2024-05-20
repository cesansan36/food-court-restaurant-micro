package plazadecomidas.restaurants.TestData;

import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.RestaurantEntity;

public class PersistenceTestData {

    private PersistenceTestData() {
        throw new IllegalStateException("Utility class");
    }

    public static RestaurantEntity getValidRestaurantEntity(Long id, Long idOwner) {
        return new RestaurantEntity(
            id,
            DomainTestData.NAME_FIELD.formatted(id),
            DomainTestData.ADDRESS_FIELD.formatted(id),
            idOwner,
            DomainTestData.PHONE_NUMBER_FIELD.formatted(id),
            DomainTestData.LOGO_URL_FIELD.formatted(id),
            DomainTestData.NIT_FIELD.formatted(id)
        );
    }
}
