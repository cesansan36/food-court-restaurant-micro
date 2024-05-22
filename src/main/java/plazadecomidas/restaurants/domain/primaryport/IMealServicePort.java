package plazadecomidas.restaurants.domain.primaryport;

import plazadecomidas.restaurants.domain.model.Meal;

public interface IMealServicePort {

    void saveMeal(Meal meal, Long userId);

    void updateMeal(Meal meal, Long userId);
}
