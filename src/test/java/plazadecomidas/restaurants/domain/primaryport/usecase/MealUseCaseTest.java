package plazadecomidas.restaurants.domain.primaryport.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import plazadecomidas.restaurants.TestData.DomainTestData;
import plazadecomidas.restaurants.domain.exception.DataMismatchException;
import plazadecomidas.restaurants.domain.exception.FieldNotFoundException;
import plazadecomidas.restaurants.domain.model.Meal;
import plazadecomidas.restaurants.domain.secondaryport.ICategoryPersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IMealPersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IRestaurantPersistencePort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MealUseCaseTest {

    private MealUseCase mealUseCase;

    private IMealPersistencePort mealPersistencePort;
    private ICategoryPersistencePort categoryPersistencePort;
    private IRestaurantPersistencePort restaurantPersistencePort;

    @BeforeEach
    void setUp() {
        mealPersistencePort = mock(IMealPersistencePort.class);
        categoryPersistencePort = mock(ICategoryPersistencePort.class);
        restaurantPersistencePort = mock(IRestaurantPersistencePort.class);
        mealUseCase = new MealUseCase(mealPersistencePort, categoryPersistencePort, restaurantPersistencePort);
    }

    @Test
    @DisplayName("Save meal successful")
    void saveMeal() {
        Meal meal = DomainTestData.getValidMeal(1L);
        Long idUser = 1L;

        when(categoryPersistencePort.existsCategory(anyLong())).thenReturn(true);
        when(restaurantPersistencePort.existsRestaurantOwnerPair(anyLong(), anyLong())).thenReturn(true);

        mealUseCase.saveMeal(meal, idUser);

        verify(mealPersistencePort, times(1)).saveMeal(any(Meal.class));
        verify(restaurantPersistencePort, times(1)).existsRestaurantOwnerPair(anyLong(), anyLong());
        verify(categoryPersistencePort, times(1)).existsCategory(anyLong());
    }

    @Test
    @DisplayName("Throw exception if restaurant owner pair not exists")
    void saveMealThrowIfRestaurantOwnerPairNotExists() {
        Meal meal = DomainTestData.getValidMeal(1L);
        Long idUser = 1L;

        when(restaurantPersistencePort.existsRestaurantOwnerPair(anyLong(), anyLong())).thenReturn(false);
        when(categoryPersistencePort.existsCategory(anyLong())).thenReturn(true);

        assertThrows(DataMismatchException.class, () -> mealUseCase.saveMeal(meal, idUser));

        verify(mealPersistencePort, times(0)).saveMeal(any(Meal.class));
        verify(restaurantPersistencePort, times(1)).existsRestaurantOwnerPair(anyLong(), anyLong());
        verify(categoryPersistencePort, times(1)).existsCategory(anyLong());
    }

    @Test
    @DisplayName("Throw exception if category not exists")
    void saveMealThrowIfCategoryNotExists() {
        Meal meal = DomainTestData.getValidMeal(1L);
        Long idUser = 1L;

        when(restaurantPersistencePort.existsRestaurantOwnerPair(anyLong(), anyLong())).thenReturn(true);
        when(categoryPersistencePort.existsCategory(anyLong())).thenReturn(false);

        assertThrows(FieldNotFoundException.class, () -> mealUseCase.saveMeal(meal, idUser));

        verify(mealPersistencePort, times(0)).saveMeal(any(Meal.class));
        verify(restaurantPersistencePort, times(0)).existsRestaurantOwnerPair(anyLong(), anyLong());
        verify(categoryPersistencePort, times(1)).existsCategory(anyLong());
    }

    @Test
    @DisplayName("Update meal successful")
    void updateMeal() {
        Meal meal = DomainTestData.getValidMeal(1L);
        Long userId = 1L;
        Meal previousMeal = DomainTestData.getValidMeal(2L);

        when(restaurantPersistencePort.existsRestaurantOwnerPair(anyLong(), anyLong())).thenReturn(true);
        when(mealPersistencePort.getByNameAndRestaurantId(anyString(), anyLong())).thenReturn(previousMeal);

        mealUseCase.updateMeal(meal, userId);

        verify(mealPersistencePort, times(1)).getByNameAndRestaurantId(anyString(), anyLong());
        verify(mealPersistencePort, times(1)).updateMeal(any(Meal.class));
    }

    @Test
    @DisplayName("Update meal availability successful")
    void updateMealAvailability() {
        Meal meal = DomainTestData.getValidMeal(1L);
        Long userId = 1L;
        Meal previousMeal = DomainTestData.getValidMeal(2L);

        when(restaurantPersistencePort.existsRestaurantOwnerPair(anyLong(), anyLong())).thenReturn(true);
        when(mealPersistencePort.getByNameAndRestaurantId(anyString(), anyLong())).thenReturn(previousMeal);

        mealUseCase.updateMealAvailability(meal, userId);

        verify(mealPersistencePort, times(1)).getByNameAndRestaurantId(anyString(), anyLong());
        verify(mealPersistencePort, times(1)).updateMeal(any(Meal.class));
    }

    @Test
    @DisplayName("Get meals of restaurant")
    void getMealsOfRestaurant() {
        Long idRestaurant = 1L;
        Meal meal = DomainTestData.getValidMeal(1L);

        when(mealPersistencePort.getMealsOfRestaurant(anyLong(), anyInt(), anyInt(), anyLong())).thenReturn(List.of(meal));

        mealUseCase.getMealsOfRestaurant(idRestaurant, 0, 10, 1L);

        verify(mealPersistencePort, times(1)).getMealsOfRestaurant(anyLong(), anyInt(), anyInt(), anyLong());
    }
}