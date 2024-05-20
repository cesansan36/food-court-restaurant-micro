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


class RestaurantTest {

    @ParameterizedTest
    @DisplayName("Should pass with correct data")
    @MethodSource("correctData")
    void shouldPassWithCorrectData(Long id, String name, String address, Long ownerId, String phoneNumber, String logoUrl, String nit) {

        String nameTrim = name.trim();
        String addressTrim = address.trim();
        String phoneNumberTrim = phoneNumber.trim();
        String logoUrlTrim = logoUrl.trim();
        String nitTrim = nit.trim();

        Restaurant restaurant = new Restaurant(id, name, address, ownerId, phoneNumber, logoUrl, nit);
        assertAll(
                () -> assertEquals(id, restaurant.getId()),
                () -> assertEquals(nameTrim, restaurant.getName()),
                () -> assertEquals(addressTrim, restaurant.getAddress()),
                () -> assertEquals(ownerId, restaurant.getOwnerId()),
                () -> assertEquals(phoneNumberTrim, restaurant.getPhoneNumber()),
                () -> assertEquals(logoUrlTrim, restaurant.getLogoUrl()),
                () -> assertEquals(nitTrim, restaurant.getNit())
        );
    }

    @ParameterizedTest
    @DisplayName("Should fail with empty fields")
    @MethodSource("dataWithNullOrEmptyFields")
    void shouldFailWithEmptyFields(Long id, String name, String address, Long ownerId, String phoneNumber, String logoUrl, String nit) {
        assertThrows(EmptyFieldException.class, () -> new Restaurant(id, name, address, ownerId, phoneNumber, logoUrl, nit));
    }

    @ParameterizedTest
    @DisplayName("Should fail with broken rule fields")
    @MethodSource("dataWithBrokenRules")
    void shouldFailWithBrokenRules(Long id, String name, String address, Long ownerId, String phoneNumber, String logoUrl, String nit) {
        assertThrows(FieldRuleInvalidException.class, () -> new Restaurant(id, name, address, ownerId, phoneNumber, logoUrl, nit));
    }

    static Stream<Arguments> correctData () {
        return Stream.of(
                Arguments.of(1L, "Restaurant1", "Street 1", 1L, "123456789", "https://example.com/logo.png", "123456789"),
                Arguments.of(null, "R35t4ur4nt", "Street 2", 2L, "+987654321", "https://example.com/logo.png", "987654321")
        );
    }

    static Stream<Arguments> dataWithNullOrEmptyFields () {
        return Stream.of(
                Arguments.of(1L, "", "Street 1", 1L, "123456789", "https://example.com/logo.png", "123456789"),
                Arguments.of(1L, "    ", "Street 1", 1L, "123456789", "https://example.com/logo.png", "123456789"),
                Arguments.of(1L, null, "Street 1", 1L, "123456789", "https://example.com/logo.png", "123456789"),
                Arguments.of(1L, "Restaurant1", "", 1L, "123456789", "https://example.com/logo.png", "123456789"),
                Arguments.of(1L, "Restaurant1", "    ", 1L, "123456789", "https://example.com/logo.png", "123456789"),
                Arguments.of(1L, "Restaurant1", null, 1L, "123456789", "https://example.com/logo.png", "123456789"),
                Arguments.of(1L, "Restaurant1", "Street 1", null, "123456789", "https://example.com/logo.png", "123456789"),
                Arguments.of(1L, "Restaurant1", "Street 1", 1L, "", "https://example.com/logo.png", "123456789"),
                Arguments.of(1L, "Restaurant1", "Street 1", 1L, "   ", "https://example.com/logo.png", "123456789"),
                Arguments.of(1L, "Restaurant1", "Street 1", 1L, null, "https://example.com/logo.png", "123456789"),
                Arguments.of(1L, "Restaurant1", "Street 1", 1L, "123456789", "", "123456789"),
                Arguments.of(1L, "Restaurant1", "Street 1", 1L, "123456789", "   ", "123456789"),
                Arguments.of(1L, "Restaurant1", "Street 1", 1L, "123456789", null, "123456789"),
                Arguments.of(1L, "Restaurant1", "Street 1", 1L, "123456789", "https://example.com/logo.png", ""),
                Arguments.of(1L, "Restaurant1", "Street 1", 1L, "123456789", "https://example.com/logo.png", "   "),
                Arguments.of(1L, "Restaurant1", "Street 1", 1L, "123456789", "https://example.com/logo.png", null)
        );
    }

    static Stream<Arguments> dataWithBrokenRules() {
        return Stream.of(
                Arguments.of(1L, "59163846951", "Street 1", 1L, "123456789", "https://example.com/logo.png", "123456789"),
                Arguments.of(null, "Restaurant 1", "Street 2", 2L, "+987654321", "https://example.com/logo.png", "987654321"),
                Arguments.of(2L, "Restaurant%@1", "Street 2", 2L, "+987654321", "https://example.com/logo.png", "987654321"),
                Arguments.of(2L, "Restaurant1", "Street 2", 2L, "-987654321", "https://example.com/logo.png", "987654321"),
                Arguments.of(2L, "Restaurant1", "Street 2", 2L, "987+654321", "https://example.com/logo.png", "987654321"),
                Arguments.of(2L, "Restaurant1", "Street 2", 2L, "%987654321", "https://example.com/logo.png", "987654321"),
                Arguments.of(2L, "Restaurant1", "Street 2", 2L, "987654321987654321", "https://example.com/logo.png", "987654321"),
                Arguments.of(2L, "Restaurant1", "Street 2", 2L, "-987654321", "https://example.com/logo.png", "a987654321")
        );
    }
}