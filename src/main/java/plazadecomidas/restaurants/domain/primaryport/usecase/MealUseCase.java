package plazadecomidas.restaurants.domain.primaryport.usecase;

import plazadecomidas.restaurants.domain.exception.DataMismatchException;
import plazadecomidas.restaurants.domain.model.Meal;
import plazadecomidas.restaurants.domain.primaryport.IMealServicePort;
import plazadecomidas.restaurants.domain.secondaryport.IMealPersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IRestaurantPersistencePort;
import plazadecomidas.restaurants.domain.util.DomainConstants;

import java.util.List;

public class MealUseCase implements IMealServicePort {

    private final IMealPersistencePort mealPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    public MealUseCase(IMealPersistencePort mealPersistencePort, IRestaurantPersistencePort restaurantPersistencePort) {
        this.mealPersistencePort = mealPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public void saveMeal(Meal meal, Long userId) {
        throwIfRestaurantOwnerPairNotExists(meal.getRestaurant().getId(), userId);
        mealPersistencePort.saveMeal(meal);
    }

    @Override
    public void updateMeal(Meal meal, Long userId) {

        throwIfRestaurantOwnerPairNotExists(meal.getRestaurant().getId(), userId);

        Meal previousMeal = mealPersistencePort.getByNameAndRestaurantId(meal.getName(), meal.getRestaurant().getId());

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

    @Override
    public void updateMealAvailability(Meal meal, Long userId) {
        throwIfRestaurantOwnerPairNotExists(meal.getRestaurant().getId(), userId);

        Meal previousMeal = mealPersistencePort.getByNameAndRestaurantId(meal.getName(), meal.getRestaurant().getId());

        Meal updatedValuesMeal = new Meal(
                previousMeal.getId(),
                previousMeal.getName(),
                previousMeal.getDescription(),
                previousMeal.getPrice(),
                previousMeal.getImageUrl(),
                meal.isActive(),
                previousMeal.getRestaurant(),
                previousMeal.getCategory());

        mealPersistencePort.updateMeal(updatedValuesMeal);
    }

    @Override
    public List<Meal> getMealsOfRestaurant(Long restaurantId) {
        return mealPersistencePort.getMealsOfRestaurant(restaurantId);
    }

    void throwIfRestaurantOwnerPairNotExists (Long idRestaurant, Long idOwner) {
        boolean isRestaurantOwnerValid = restaurantPersistencePort.existsRestaurantOwnerPair(idRestaurant, idOwner);

        if(!isRestaurantOwnerValid) {
            throw new DataMismatchException(DomainConstants.RESTAURANT_OWNER_MISMATCH_MESSAGE);
        }
    }
}
