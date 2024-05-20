package plazadecomidas.restaurants.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import plazadecomidas.restaurants.domain.exception.EmptyFieldException;
import plazadecomidas.restaurants.domain.exception.FieldRuleInvalidException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class MealTest {

    @ParameterizedTest
    @DisplayName("Regular behavior")
    @MethodSource("correctData")
    void regularBehavior(Long id, String name, String description, Long price, String imageUrl, boolean isActive) {
        Restaurant restaurant = mock(Restaurant.class);
        Category category = mock(Category.class);

        Meal meal = new Meal(id,  name, description, price, imageUrl, isActive, restaurant, category);
        assertAll(
                () -> assertEquals(id, meal.getId()),
                () -> assertEquals(name, meal.getName()),
                () -> assertEquals(description, meal.getDescription()),
                () -> assertEquals(price, meal.getPrice()),
                () -> assertEquals(imageUrl, meal.getImageUrl()),
                () -> assertEquals(isActive, meal.isActive()),
                () -> assertEquals(restaurant, meal.getRestaurant()),
                () -> assertEquals(category, meal.getCategory())
        );
    }

    @ParameterizedTest
    @DisplayName("Should fail with empty fields")
    @MethodSource("dataWithEmptyOrNullValues")
    void shouldFailWithEmptyFields(Long id, String name, String description, Long price, String imageUrl, boolean isActive, Restaurant restaurant, Category category) {
        assertThrows(EmptyFieldException.class, () -> new Meal(id,  name, description, price, imageUrl, isActive, restaurant, category));
    }

    @ParameterizedTest
    @DisplayName("Should fail with broken rule fields")
    @MethodSource("dataWithBrokenRules")
    void shouldFailWithBrokenRules(Long id, String name, String description, Long price, String imageUrl, boolean isActive, Restaurant restaurant, Category category) {
        assertThrows(FieldRuleInvalidException.class, () -> new Meal(id,  name, description, price, imageUrl, isActive, restaurant, category));
    }

    static Stream<Arguments> correctData() {
        return Stream.of(
                Arguments.of(1L,  "Chocolate", "Delicious", 159L, "https://example.com/image.png", true),
                Arguments.of(null,  "Chocolate", "Delicious", 200L, "https://example.com/image.png", false)
        );
    }

    static Stream<Arguments> dataWithEmptyOrNullValues () {
        return Stream.of(
                Arguments.of(1L,  "", "Delicious", 159L, "https://example.com/image.png", true, mock(Restaurant.class), mock(Category.class)),
                Arguments.of(1L,  "    ", "Delicious", 159L, "https://example.com/image.png", true, mock(Restaurant.class), mock(Category.class)),
                Arguments.of(1L,  null, "Delicious", 159L, "https://example.com/image.png", true, mock(Restaurant.class), mock(Category.class)),
                Arguments.of(1L,  "Chocolate", "", 159L, "https://example.com/image.png", true, mock(Restaurant.class), mock(Category.class)),
                Arguments.of(1L,  "Chocolate", "   ", 159L, "https://example.com/image.png", true, mock(Restaurant.class), mock(Category.class)),
                Arguments.of(1L,  "Chocolate", null, 159L, "https://example.com/image.png", true, mock(Restaurant.class), mock(Category.class)),
                Arguments.of(1L,  "Chocolate", "Delicious", null, "https://example.com/image.png", true, mock(Restaurant.class), mock(Category.class)),
                Arguments.of(1L,  "Chocolate", "Delicious", 159L, "", true, mock(Restaurant.class), mock(Category.class)),
                Arguments.of(1L,  "Chocolate", "Delicious", 159L, "   ", true, mock(Restaurant.class), mock(Category.class)),
                Arguments.of(1L,  "Chocolate", "Delicious", 159L, null, true, mock(Restaurant.class), mock(Category.class)),
                Arguments.of(1L,  "Chocolate", "Delicious", 159L, "https://example.com/image.png", true, null, mock(Category.class)),
                Arguments.of(1L,  "Chocolate", "Delicious", 159L, "https://example.com/image.png", true, mock(Restaurant.class), null)
        );
    }

    static Stream<Arguments> dataWithBrokenRules () {
        return Stream.of(
                Arguments.of(1L,  "Chocolate", "Delicious", 0L, "https://example.com/image.png", true, mock(Restaurant.class), mock(Category.class)),
                Arguments.of(1L,  "Chocolate", "Delicious", -1L, "https://example.com/image.png", true, mock(Restaurant.class), mock(Category.class))
        );
    }
}