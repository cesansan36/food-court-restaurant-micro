package plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import plazadecomidas.restaurants.TestData.DomainTestData;
import plazadecomidas.restaurants.TestData.PersistenceTestData;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.MealEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryAlreadyExistsException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryNotFoundException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IMealEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IMealRepository;
import plazadecomidas.restaurants.domain.model.Meal;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MealAdapterTest {

    private MealAdapter mealAdapter;

    private IMealRepository mealRepository;
    private IMealEntityMapper mealEntityMapper;

    @BeforeEach
    void setUp() {
        mealRepository = mock(IMealRepository.class);
        mealEntityMapper = mock(IMealEntityMapper.class);
        mealAdapter = new MealAdapter(mealRepository, mealEntityMapper);
    }

    @Test
    @DisplayName("Should save Meal normally")
    void shouldSaveMealNormally() {
        MealEntity mealEntity = mock(MealEntity.class);
        Meal meal = DomainTestData.getValidMeal(1L);

        when(mealRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(mealEntityMapper.toEntity(any(Meal.class))).thenReturn(mealEntity);

        mealAdapter.saveMeal(meal);

        verify(mealRepository, times(1)).findByName(anyString());
        verify(mealEntityMapper, times(1)).toEntity(any(Meal.class));
        verify(mealRepository, times(1)).save(any(MealEntity.class));
    }

    @Test
    @DisplayName("Should fail on already exists")
    void shouldFailOnAlreadyExists() {
        MealEntity mealEntity = mock(MealEntity.class);
        Meal meal = DomainTestData.getValidMeal(1L);

        when(mealRepository.findByName(anyString())).thenReturn(Optional.of(mealEntity));

        assertThrows(RegistryAlreadyExistsException.class, () -> mealAdapter.saveMeal(meal));

        verify(mealRepository, times(1)).findByName(anyString());
        verify(mealEntityMapper, times(0)).toEntity(any(Meal.class));
        verify(mealRepository, times(0)).save(any(MealEntity.class));

    }

    @Test
    @DisplayName("Get by name successful")
    void getByName() {
        MealEntity mealEntity = PersistenceTestData.getValidMealEntity(1L);
        Meal meal = DomainTestData.getValidMeal(1L);

        when(mealRepository.findByName(anyString())).thenReturn(Optional.of(mealEntity));
        when(mealEntityMapper.toDomain(any(MealEntity.class))).thenReturn(meal);

        Meal foundMeal = mealAdapter.getByName("name1");

        assertAll(
            () -> verify(mealRepository, times(1)).findByName(anyString()),
            () -> verify(mealEntityMapper, times(1)).toDomain(any(MealEntity.class)),
            () -> assertEquals(meal.getName(), foundMeal.getName()),
            () -> assertEquals(meal.getDescription(), foundMeal.getDescription()),
            () -> assertEquals(meal.getPrice(), foundMeal.getPrice()),
            () -> assertEquals(meal.getImageUrl(), foundMeal.getImageUrl()),
            () -> assertEquals(meal.isActive(), foundMeal.isActive())
        );
    }

    @Test
    @DisplayName("Fail get by name because not found")
    void failGetByNameBecauseNotFound() {
        when(mealRepository.findByName(anyString())).thenReturn(Optional.empty());
        assertThrows(RegistryNotFoundException.class, () -> mealAdapter.getByName("name1"));
    }

    @Test
    @DisplayName("Update meal successful")
    void updateMeal() {
        Meal meal = DomainTestData.getValidMeal(1L);
        MealEntity mealEntity = PersistenceTestData.getValidMealEntity(1L);

        when(mealRepository.findByName(anyString())).thenReturn(Optional.of(mealEntity));
        when(mealEntityMapper.toEntity(any(Meal.class))).thenReturn(mealEntity);

        mealAdapter.updateMeal(meal);

        verify(mealRepository, times(1)).findByName(anyString());
        verify(mealEntityMapper, times(1)).toEntity(any(Meal.class));
        verify(mealRepository, times(1)).save(any(MealEntity.class));
    }

    @Test
    @DisplayName("Fail update because not found")
    void failUpdateBecauseNotFound() {
        Meal meal = DomainTestData.getValidMeal(1L);

        when(mealRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(RegistryNotFoundException.class, () -> mealAdapter.updateMeal(meal));

        verify(mealRepository, times(1)).findByName(anyString());
        verify(mealEntityMapper, times(0)).toEntity(any(Meal.class));
        verify(mealRepository, times(0)).save(any(MealEntity.class));
    }
}