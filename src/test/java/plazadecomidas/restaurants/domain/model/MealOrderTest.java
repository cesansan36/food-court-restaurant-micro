package plazadecomidas.restaurants.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import plazadecomidas.restaurants.domain.exception.EmptyFieldException;
import plazadecomidas.restaurants.domain.exception.FieldRuleInvalidException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MealOrderTest {

    @ParameterizedTest
    @DisplayName("Should createRecord meal order correctly")
    @MethodSource("correctData")
    void shouldCreateMealOrderCorrectly(Long idMeal, Long idOrder, Integer quantity) {
        MealOrder mealOrder = new MealOrder(idMeal, idOrder, quantity);
        assertEquals(idMeal, mealOrder.getIdMeal());
        assertEquals(idOrder, mealOrder.getIdOrder());
        assertEquals(quantity, mealOrder.getQuantity());

    }

    @ParameterizedTest
    @DisplayName("Should throw an exception when creating meal order with null values")
    @MethodSource("dataWithNullValues")
    void shouldThrowExceptionWhenCreatingMealOrderWithNullValues(Long idMeal, Long idOrder, Integer quantity) {
        assertThrows(EmptyFieldException.class, () -> new MealOrder(idMeal, idOrder, quantity));
    }

    @ParameterizedTest
    @DisplayName("Should throw an exception when creating meal order with broken rules")
    @MethodSource("dataWithBrokenRules")
    void shouldThrowExceptionWhenCreatingMealOrderWithBrokenRules(Long idMeal, Long idOrder, Integer quantity) {
        assertThrows(FieldRuleInvalidException.class, () -> new MealOrder(idMeal, idOrder, quantity));
    }

    static Stream<Arguments> correctData() {
        return Stream.of(
                Arguments.of(1L, 1L, 1),
                Arguments.of(20L, null, 15)
        );
    }

    static Stream<Arguments> dataWithBrokenRules() {
        return Stream.of(
                Arguments.of(2L, 1L, 0),
                Arguments.of(1L, null, -20)
        );
    }

    static Stream<Arguments> dataWithNullValues() {
        return Stream.of(
                Arguments.of(null, 1L, 1),
                Arguments.of(1L, 1L, null)
        );
    }
}