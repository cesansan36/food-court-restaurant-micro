package plazadecomidas.restaurants.domain.secondaryport;

import plazadecomidas.restaurants.domain.model.Meal;

public interface IMealPersistencePort {

    void saveMeal(Meal meal);

    Meal getByName(String name);

    void updateMeal(Meal meal);
}
