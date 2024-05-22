package plazadecomidas.restaurants.domain.primaryport.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import plazadecomidas.restaurants.TestData.DomainTestData;
import plazadecomidas.restaurants.domain.exception.DataMismatchException;
import plazadecomidas.restaurants.domain.model.Meal;
import plazadecomidas.restaurants.domain.secondaryport.IMealPersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IRestaurantPersistencePort;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MealUseCaseTest {

    private MealUseCase mealUseCase;

    private IMealPersistencePort mealPersistencePort;
    private IRestaurantPersistencePort restaurantPersistencePort;

    @BeforeEach
    void setUp() {
        mealPersistencePort = mock(IMealPersistencePort.class);
        restaurantPersistencePort = mock(IRestaurantPersistencePort.class);
        mealUseCase = new MealUseCase(mealPersistencePort, restaurantPersistencePort);
    }

    @Test
    @DisplayName("Save meal successful")
    void saveMeal() {
        Meal meal = DomainTestData.getValidMeal(1L);
        Long idUser = 1L;

        when(restaurantPersistencePort.existsRestaurantOwnerPair(anyLong(), anyLong())).thenReturn(true);

        mealUseCase.saveMeal(meal, idUser);

        verify(mealPersistencePort, times(1)).saveMeal(any(Meal.class));
        verify(restaurantPersistencePort, times(1)).existsRestaurantOwnerPair(anyLong(), anyLong());
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
    @DisplayName("Throw exception if restaurant owner pair not exists")
    void throwIfRestaurantOwnerPairNotExists() {
        Meal meal = DomainTestData.getValidMeal(1L);
        Long idUser = 1L;

        when(restaurantPersistencePort.existsRestaurantOwnerPair(anyLong(), anyLong())).thenReturn(false);

        assertThrows(DataMismatchException.class, () -> mealUseCase.saveMeal(meal, idUser));

        verify(mealPersistencePort, times(0)).saveMeal(any(Meal.class));
        verify(restaurantPersistencePort, times(1)).existsRestaurantOwnerPair(anyLong(), anyLong());
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
}