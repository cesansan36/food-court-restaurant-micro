package plazadecomidas.restaurants.domain.primaryport;

import plazadecomidas.restaurants.domain.model.Meal;

public interface IMealServicePort {

    void saveMeal(Meal meal);

    void updateMeal(Meal meal);
}
