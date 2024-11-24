package plazadecomidas.restaurants.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.response.MealResponse;
import plazadecomidas.restaurants.domain.model.Meal;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IMealResponseMapper {

    MealResponse mealToMealResponse(Meal meal);

    List<MealResponse> mealsToMealResponses(List<Meal> meals);
}
