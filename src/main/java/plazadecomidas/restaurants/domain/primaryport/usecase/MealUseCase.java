package plazadecomidas.restaurants.domain.primaryport.usecase;

import plazadecomidas.restaurants.domain.model.Meal;
import plazadecomidas.restaurants.domain.primaryport.IMealServicePort;
import plazadecomidas.restaurants.domain.secondaryport.IMealPersistencePort;

public class MealUseCase implements IMealServicePort {

    private final IMealPersistencePort mealPersistencePort;

    public MealUseCase(IMealPersistencePort mealPersistencePort) {
        this.mealPersistencePort = mealPersistencePort;
    }

    @Override
    public void saveMeal(Meal meal) {
        mealPersistencePort.saveMeal(meal);
    }

    @Override
    public void updateMeal(Meal meal) {

        Meal previousMeal = mealPersistencePort.getByName(meal.getName());

        Meal updatedValuesMeal = new Meal(
                previousMeal.getId(),
                previousMeal.getName(),
                meal.getDescription(),
                meal.getPrice(),
                previousMeal.getImageUrl(),
                previousMeal.isActive(),
                previousMeal.getRestaurant(),
                previousMeal.getCategory());

        mealPersistencePort.updateMeal(updatedValuesMeal);
    }
}
