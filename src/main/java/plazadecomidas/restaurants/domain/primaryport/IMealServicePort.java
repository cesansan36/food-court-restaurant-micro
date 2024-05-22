package plazadecomidas.restaurants.domain.primaryport;

import plazadecomidas.restaurants.domain.model.Meal;

import java.util.List;

public interface IMealServicePort {

    void saveMeal(Meal meal, Long userId);

    void updateMeal(Meal meal, Long userId);

    void updateMealAvailability(Meal meal, Long userId);

    List<Meal> getMealsOfRestaurant(Long restaurantId);
}
