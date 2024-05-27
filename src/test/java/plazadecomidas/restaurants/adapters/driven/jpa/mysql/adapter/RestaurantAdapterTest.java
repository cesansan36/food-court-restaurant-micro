package plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import plazadecomidas.restaurants.TestData.DomainTestData;
import plazadecomidas.restaurants.TestData.PersistenceTestData;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryAlreadyExistsException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryNotFoundException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IRestaurantEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IRestaurantRepository;
import plazadecomidas.restaurants.domain.model.Restaurant;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @Test
    @DisplayName("restaurant owner pair found")
    void existsRestaurantOwnerPair() {
        RestaurantEntity restaurantEntity = mock(RestaurantEntity.class);

        when(restaurantRepository.findByIdAndOwnerId(anyLong(), anyLong())).thenReturn(Optional.of(restaurantEntity));

        restaurantAdapter.existsRestaurantOwnerPair(1L, 1L);

        verify(restaurantRepository, times(1)).findByIdAndOwnerId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("restaurant owner pair not found")
    void existsRestaurantOwnerPairFail() {
        when(restaurantRepository.findByIdAndOwnerId(anyLong(), anyLong())).thenReturn(Optional.empty());

        assertThrows(RegistryNotFoundException.class, () -> restaurantAdapter.existsRestaurantOwnerPair(1L, 1L));

        verify(restaurantRepository, times(1)).findByIdAndOwnerId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Get all restaurants")
    void getAllRestaurants() {
        List<RestaurantEntity> restaurantEntities = List.of(PersistenceTestData.getValidRestaurantEntity(1L, 1L));
        List<Restaurant> restaurants = List.of(DomainTestData.getValidRestaurant(1L, 1L));

        when(restaurantRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(restaurantEntities));
        when(restaurantEntityMapper.toDomainList(anyList())).thenReturn(restaurants);

        restaurantAdapter.getAllRestaurants(1, 1);
        verify(restaurantRepository, times(1)).findAll(any(Pageable.class));
    }

    @ParameterizedTest
    @CsvSource({ "true", "false" })
    @DisplayName("restaurant owner match found")
    void existsOwnerRestaurantMatch(boolean expected) {

        when(restaurantRepository.existsByOwnerIdAndId(anyLong(), anyLong())).thenReturn(expected);

        boolean result = restaurantAdapter.existsOwnerRestaurantMatch(1L, 1L);

        assertEquals(expected, result);
        verify(restaurantRepository, times(1)).existsByOwnerIdAndId(anyLong(), anyLong());
    }
}