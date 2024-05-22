package plazadecomidas.restaurants.domain.secondaryport;

import plazadecomidas.restaurants.domain.model.Meal;

import java.util.List;

public interface IMealPersistencePort {

    void saveMeal(Meal meal);

    Meal getByName(String name);

    void updateMeal(Meal meal);

    Meal getByNameAndRestaurantId(String name, Long id);

    List<Meal> getMealsOfRestaurant(Long restaurantId);
}
