package plazadecomidas.restaurants.domain.primaryport.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import plazadecomidas.restaurants.TestData.DomainTestData;
import plazadecomidas.restaurants.domain.model.Meal;
import plazadecomidas.restaurants.domain.secondaryport.IMealPersistencePort;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MealUseCaseTest {

    private MealUseCase mealUseCase;

    private IMealPersistencePort mealPersistencePort;

    @BeforeEach
    void setUp() {
        mealPersistencePort = mock(IMealPersistencePort.class);
        mealUseCase = new MealUseCase(mealPersistencePort);
    }

    @Test
    @DisplayName("Save meal successful")
    void saveMeal() {
        Meal meal = mock(Meal.class);

        mealUseCase.saveMeal(meal);

        verify(mealPersistencePort, times(1)).saveMeal(any(Meal.class));
    }

    @Test
    @DisplayName("Update meal successful")
    void updateMeal() {
        Meal meal = DomainTestData.getValidMeal(1L);
        Meal previousMeal = DomainTestData.getValidMeal(2L);

        when(mealPersistencePort.getByName(meal.getName())).thenReturn(previousMeal);

        mealUseCase.updateMeal(meal);

        verify(mealPersistencePort, times(1)).getByName(meal.getName());
        verify(mealPersistencePort, times(1)).updateMeal(any(Meal.class));
    }
}