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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class OrderTest {

    @ParameterizedTest
    @MethodSource("validData")
    @DisplayName("Order should be created successfully")
    void shouldCreateOrder(Long id, Long idClient, LocalDate date, String status, Integer securityPin, Long idChef, Long idRestaurant, List<MealOrder> mealOrders) {
        Order order = new Order(id, idClient, date, status, securityPin, idChef, idRestaurant, mealOrders);

        assertAll(
                () -> assertEquals(id, order.getId()),
                () -> assertEquals(idClient, order.getIdClient()),
                () -> assertEquals(date, order.getDate()),
                () -> assertEquals(status, order.getStatus()),
                () -> assertEquals(securityPin, order.getSecurityPin()),
                () -> assertEquals(idChef, order.getIdChef()),
                () -> assertEquals(idRestaurant, order.getIdRestaurant()),
                () -> assertEquals(mealOrders, order.getMeals())
        );
    }

    @ParameterizedTest
    @MethodSource("dataWithEmptyOrNullFields")
    @DisplayName("Should fail with empty fields")
    void shouldFailWithEmptyFields(Long id, Long idClient, LocalDate date, String status, Integer securityPin, Long idChef, Long idRestaurant, List<MealOrder> mealOrders) {
        assertThrows(EmptyFieldException.class, () -> new Order(id, idClient, date, status, securityPin, idChef, idRestaurant, mealOrders));
    }

    @ParameterizedTest
    @MethodSource("dataWithBrokenRules")
    @DisplayName("Should fail with broken rule fields")
    void shouldFailWithBrokenRules(Long id, Long idClient, LocalDate date, String status, Integer securityPin, Long idChef, Long idRestaurant, List<MealOrder> mealOrders) {
        assertThrows(FieldRuleInvalidException.class, () -> new Order(id, idClient, date, status, securityPin, idChef, idRestaurant, mealOrders));
    }

    static Stream<Arguments> validData () {
        return Stream.of(
                Arguments.of(1L, 1L, LocalDate.now(), "PENDING", 1111, 1L, 1L, List.of(mock(MealOrder.class))),
                Arguments.of(1L, 1L, LocalDate.now(), "PREPARING", 1111, null, 1L, List.of(mock(MealOrder.class), mock(MealOrder.class))),
                Arguments.of(1L, 1L, LocalDate.now(), "READY", 1111, null, 1L, List.of(mock(MealOrder.class), mock(MealOrder.class))),
                Arguments.of(1L, 1L, LocalDate.now(), "DELIVERED", 1111, null, 1L, List.of(mock(MealOrder.class), mock(MealOrder.class))),
                Arguments.of(1L, 1L, LocalDate.now(), "CANCELED", 1111, null, 1L, List.of(mock(MealOrder.class), mock(MealOrder.class)))
        );
    }

    static Stream<Arguments> dataWithEmptyOrNullFields() {
        return Stream.of(
                Arguments.of(1L, null, LocalDate.now(), "PENDING", 1111, 1L, 1L, List.of(mock(MealOrder.class))),
                Arguments.of(1L, 1L, null, "PENDING", 1111, 1L, 1L, List.of(mock(MealOrder.class))),
                Arguments.of(1L, 1L, LocalDate.now(), null, 1111, 1L, 1L, List.of(mock(MealOrder.class))),
                Arguments.of(1L, 1L, LocalDate.now(), "PENDING", 1111, 1L, null, List.of(mock(MealOrder.class))),
                Arguments.of(1L, 1L, LocalDate.now(), "PENDING", 1111, 1L, 1L, null),
                Arguments.of(1L, 1L, LocalDate.now(), "        ", 1111, 1L, 1L, List.of(mock(MealOrder.class)))
        );
    }

    static Stream<Arguments> dataWithBrokenRules() {
        return Stream.of(
                Arguments.of(1L, 1L, LocalDate.now(), "PLAYING", 1111, 1L, 1L, List.of(mock(MealOrder.class)))
        );
    }
}