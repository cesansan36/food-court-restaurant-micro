package plazadecomidas.restaurants.domain.primaryport.usecase;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import plazadecomidas.restaurants.TestData.DomainTestData;
import plazadecomidas.restaurants.adapters.driven.feign.IUserFeignClient;
import plazadecomidas.restaurants.domain.exception.FieldRuleInvalidException;
import plazadecomidas.restaurants.domain.model.Restaurant;
import plazadecomidas.restaurants.domain.secondaryport.IRestaurantPersistencePort;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    private IUserFeignClient userFeignClient;

    @BeforeEach
    void setUp() {
        restaurantPersistencePort = mock(IRestaurantPersistencePort.class);
        userFeignClient = mock(IUserFeignClient.class);
        restaurantUseCase = new RestaurantUseCase(restaurantPersistencePort, userFeignClient);
    }

    @Test
    @DisplayName("Save Restaurant successful")
    void saveRestaurant() {

        Restaurant restaurant = DomainTestData.getValidRestaurant(1L, 1L);
        ResponseEntity<Boolean> isUserValid = ResponseEntity.ok(Boolean.TRUE);

        when(userFeignClient.verifyRole(anyLong(), anyString())).thenReturn(isUserValid);

        restaurantUseCase.saveRestaurant(restaurant);

        verify(userFeignClient, times(1)).verifyRole(anyLong(), anyString());
        verify(restaurantPersistencePort, times(1)).saveRestaurant(any(Restaurant.class));
    }

    @Test
    @DisplayName("Save Restaurant fail - User not found")
    void saveRestaurantUserNotFound() {

        Restaurant restaurant = DomainTestData.getValidRestaurant(1L, 1L);
        doThrow(new RuntimeException()).when(userFeignClient).verifyRole(anyLong(), anyString());

        assertThrows(FieldRuleInvalidException.class, () -> restaurantUseCase.saveRestaurant(restaurant));
        verify(userFeignClient, times(1)).verifyRole(anyLong(), anyString());
        verify(restaurantPersistencePort, times(0)).saveRestaurant(any(Restaurant.class));
    }

    @Test
    @DisplayName("Save Restaurant fail - User not valid role")
    void saveRestaurantUserNotValidRole() {

        Restaurant restaurant = DomainTestData.getValidRestaurant(1L, 1L);
        ResponseEntity<Boolean> isUserValid = ResponseEntity.ok(Boolean.FALSE);

        when(userFeignClient.verifyRole(anyLong(), anyString())).thenReturn(isUserValid);

        assertThrows(FieldRuleInvalidException.class, () -> restaurantUseCase.saveRestaurant(restaurant));
        verify(userFeignClient, times(1)).verifyRole(anyLong(), anyString());
        verify(restaurantPersistencePort, times(0)).saveRestaurant(any(Restaurant.class));
    }
}