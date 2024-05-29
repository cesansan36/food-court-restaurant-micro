package plazadecomidas.restaurants.domain.primaryport.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import plazadecomidas.restaurants.TestData.DomainTestData;
import plazadecomidas.restaurants.domain.exception.FieldRuleInvalidException;
import plazadecomidas.restaurants.domain.model.Restaurant;
import plazadecomidas.restaurants.domain.secondaryport.IRestaurantPersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IUserConnectionPort;
import plazadecomidas.restaurants.domain.util.DomainConstants;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestaurantUseCaseTest {

    private RestaurantUseCase restaurantUseCase;

    private IRestaurantPersistencePort restaurantPersistencePort;
    private IUserConnectionPort userConnectionPort;

    @BeforeEach
    void setUp() {
        restaurantPersistencePort = mock(IRestaurantPersistencePort.class);
        userConnectionPort = mock(IUserConnectionPort.class);
        restaurantUseCase = new RestaurantUseCase(restaurantPersistencePort, userConnectionPort);
    }

    @Test
    @DisplayName("Save Restaurant successful")
    void saveRestaurant() {

        Restaurant restaurant = DomainTestData.getValidRestaurant(1L, 1L);
        boolean isUserValid = true;

        when(userConnectionPort.verifyRole(anyLong(), anyString())).thenReturn(isUserValid);

        restaurantUseCase.saveRestaurant(restaurant);

        verify(userConnectionPort, times(1)).verifyRole(anyLong(), anyString());
        verify(restaurantPersistencePort, times(1)).saveRestaurant(any(Restaurant.class));
    }

    @Test
    @DisplayName("Save Restaurant fail - User not found")
    void saveRestaurantUserNotFound() {

        Restaurant restaurant = DomainTestData.getValidRestaurant(1L, 1L);

        doThrow(new RuntimeException()).when(userConnectionPort).verifyRole(anyLong(), anyString());

        assertThrows(FieldRuleInvalidException.class, () -> restaurantUseCase.saveRestaurant(restaurant));
        verify(userConnectionPort, times(1)).verifyRole(anyLong(), anyString());
        verify(restaurantPersistencePort, times(0)).saveRestaurant(any(Restaurant.class));
    }

    @Test
    @DisplayName("Save Restaurant fail - User not valid role")
    void saveRestaurantUserNotValidRole() {

        Restaurant restaurant = DomainTestData.getValidRestaurant(1L, 1L);
        boolean isUserValid = false;

        when(userConnectionPort.verifyRole(anyLong(), anyString())).thenReturn(isUserValid);

        assertThrows(FieldRuleInvalidException.class, () -> restaurantUseCase.saveRestaurant(restaurant));
        verify(userConnectionPort, times(1)).verifyRole(anyLong(), anyString());
        verify(restaurantPersistencePort, times(0)).saveRestaurant(any(Restaurant.class));
    }

    @Test
    @DisplayName("Get listRecords of restaurants")
    void getAllRestaurants() {
        List<Restaurant> restaurants = List.of(DomainTestData.getValidRestaurant(1L, 1L));
        when(restaurantPersistencePort.getAllRestaurants(anyInt(), anyInt())).thenReturn(restaurants);

        List<Restaurant> result = restaurantUseCase.getAllRestaurants(1, 1);

        assertEquals(restaurants, result);
        verify(restaurantPersistencePort, times(1)).getAllRestaurants(anyInt(), anyInt());
    }
}