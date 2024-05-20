package plazadecomidas.restaurants.TestData;

import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.CategoryEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.MealEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.RestaurantEntity;

import static org.mockito.Mockito.mock;

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
            DomainTestData.IMG_URL_FIELD.formatted(id),
            DomainTestData.NIT_FIELD.formatted(id)
        );
    }

    public static MealEntity getValidMealEntity (Long id) {
        return new MealEntity(
            id,
            DomainTestData.NAME_FIELD.formatted(id),
            DomainTestData.DESCRIPTION_FIELD.formatted(id),
            DomainTestData.PRICE+id,
            DomainTestData.IMG_URL_FIELD.formatted(id),
            true,
            mock(RestaurantEntity.class),
            mock(CategoryEntity.class)
        );

    }
}
