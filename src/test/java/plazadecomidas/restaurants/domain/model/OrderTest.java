package plazadecomidas.restaurants.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import plazadecomidas.restaurants.domain.exception.EmptyFieldException;
import plazadecomidas.restaurants.domain.exception.FieldRuleInvalidException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class OrderTest {

    @ParameterizedTest
    @MethodSource("validData")
    @DisplayName("Order should be created successfully")
    void shouldCreateOrder( Long id, Long idClient, LocalDate date, String status, Long idChef, Long idRestaurant, List<MealOrder> mealOrders) {
        Order order = new Order(id, idClient, date, status, idChef, idRestaurant, mealOrders);

        assertEquals(id, order.getId());
        assertEquals(idClient, order.getIdClient());
        assertEquals(date, order.getDate());
        assertEquals(status, order.getStatus());
        assertEquals(idChef, order.getIdChef());
        assertEquals(idRestaurant, order.getIdRestaurant());
        assertEquals(mealOrders, order.getMeals());
    }

    @ParameterizedTest
    @MethodSource("dataWithEmptyOrNullFields")
    @DisplayName("Should fail with empty fields")
    void shouldFailWithEmptyFields(Long id, Long idClient, LocalDate date, String status, Long idChef, Long idRestaurant, List<MealOrder> mealOrders) {
        assertThrows(EmptyFieldException.class, () -> new Order(id, idClient, date, status, idChef, idRestaurant, mealOrders));
    }

    @ParameterizedTest
    @MethodSource("dataWithBrokenRules")
    @DisplayName("Should fail with broken rule fields")
    void shouldFailWithBrokenRules(Long id, Long idClient, LocalDate date, String status, Long idChef, Long idRestaurant, List<MealOrder> mealOrders) {
        assertThrows(FieldRuleInvalidException.class, () -> new Order(id, idClient, date, status, idChef, idRestaurant, mealOrders));
    }

    static Stream<Arguments> validData () {
        return Stream.of(
                Arguments.of(1L, 1L, LocalDate.now(), "PENDING", 1L, 1L, List.of(mock(MealOrder.class))),
                Arguments.of(1L, 1L, LocalDate.now(), "PREPARING", null, 1L, List.of(mock(MealOrder.class), mock(MealOrder.class))),
                Arguments.of(1L, 1L, LocalDate.now(), "READY", null, 1L, List.of(mock(MealOrder.class), mock(MealOrder.class))),
                Arguments.of(1L, 1L, LocalDate.now(), "DELIVERED", null, 1L, List.of(mock(MealOrder.class), mock(MealOrder.class))),
                Arguments.of(1L, 1L, LocalDate.now(), "CANCELED", null, 1L, List.of(mock(MealOrder.class), mock(MealOrder.class)))
        );
    }

    static Stream<Arguments> dataWithEmptyOrNullFields() {
        return Stream.of(
                Arguments.of(1L, null, LocalDate.now(), "PENDING", 1L, 1L, List.of(mock(MealOrder.class))),
                Arguments.of(1L, 1L, null, "PENDING", 1L, 1L, List.of(mock(MealOrder.class))),
                Arguments.of(1L, 1L, LocalDate.now(), null, 1L, 1L, List.of(mock(MealOrder.class))),
                Arguments.of(1L, 1L, LocalDate.now(), "PENDING", 1L, null, List.of(mock(MealOrder.class))),
                Arguments.of(1L, 1L, LocalDate.now(), "PENDING", 1L, 1L, null),
                Arguments.of(1L, 1L, LocalDate.now(), "        ", 1L, 1L, List.of(mock(MealOrder.class)))
        );
    }

    static Stream<Arguments> dataWithBrokenRules() {
        return Stream.of(
                Arguments.of(1L, 1L, LocalDate.now(), "PLAYING", 1L, 1L, List.of(mock(MealOrder.class)))
        );
    }
}