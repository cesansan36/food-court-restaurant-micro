package plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import plazadecomidas.restaurants.TestData.DomainTestData;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.MealEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryAlreadyExistsException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IMealEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IMealRepository;
import plazadecomidas.restaurants.domain.model.Meal;

import java.util.Optional;

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
}