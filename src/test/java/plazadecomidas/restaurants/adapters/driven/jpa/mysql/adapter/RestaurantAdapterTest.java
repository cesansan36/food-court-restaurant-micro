package plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import plazadecomidas.restaurants.TestData.DomainTestData;
import plazadecomidas.restaurants.TestData.PersistenceTestData;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryAlreadyExistsException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IRestaurantEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IRestaurantRepository;
import plazadecomidas.restaurants.domain.model.Restaurant;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestaurantAdapterTest {

    private RestaurantAdapter restaurantAdapter;

    private IRestaurantRepository restaurantRepository;
    private IRestaurantEntityMapper restaurantEntityMapper;

    @BeforeEach
    void setUp() {
        restaurantRepository = mock(IRestaurantRepository.class);
        restaurantEntityMapper = mock(IRestaurantEntityMapper.class);
        restaurantAdapter = new RestaurantAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Test
    @DisplayName("Should save restaurant successfully")
    void shouldSaveRestaurant() {

        Restaurant restaurant = DomainTestData.getValidRestaurant(1L, 1L);
        RestaurantEntity restaurantEntity = PersistenceTestData.getValidRestaurantEntity(1L, 1L);

        when(restaurantRepository.findByAddress(anyString())).thenReturn(Optional.empty());
        when(restaurantEntityMapper.toEntity(any(Restaurant.class))).thenReturn(restaurantEntity);

        restaurantAdapter.saveRestaurant(restaurant);

        assertAll(
            () -> verify(restaurantRepository, times(1)).findByAddress(anyString()),
            () -> verify(restaurantEntityMapper, times(1)).toEntity(any(Restaurant.class)),
            () -> verify(restaurantRepository, times(1)).save(any(RestaurantEntity.class))
        );
    }

    @Test
    @DisplayName("Should fail because the registry already exists")
    void shouldFailBecauseTheRegistryAlreadyExists() {

        Restaurant restaurant = DomainTestData.getValidRestaurant(1L, 1L);
        RestaurantEntity restaurantEntity = PersistenceTestData.getValidRestaurantEntity(1L, 1L);

        when(restaurantRepository.findByAddress(anyString())).thenReturn(Optional.of(restaurantEntity));

        assertThrows(RegistryAlreadyExistsException.class, () -> restaurantAdapter.saveRestaurant(restaurant));

        assertAll(
                () -> verify(restaurantRepository, times(1)).findByAddress(anyString()),
                () -> verify(restaurantEntityMapper, times(0)).toEntity(any(Restaurant.class)),
                () -> verify(restaurantRepository, times(0)).save(any(RestaurantEntity.class))
        );
    }
}