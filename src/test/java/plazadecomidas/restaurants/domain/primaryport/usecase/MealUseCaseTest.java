package plazadecomidas.restaurants.domain.primaryport.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import plazadecomidas.restaurants.domain.model.Meal;
import plazadecomidas.restaurants.domain.secondaryport.IMealPersistencePort;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
}