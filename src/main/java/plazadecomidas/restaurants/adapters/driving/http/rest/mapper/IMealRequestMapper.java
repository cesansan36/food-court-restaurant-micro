package plazadecomidas.restaurants.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.AddMealRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.UpdateMealRequest;
import plazadecomidas.restaurants.domain.model.Category;
import plazadecomidas.restaurants.domain.model.Meal;
import plazadecomidas.restaurants.domain.model.Restaurant;

@Mapper(componentModel = "spring")
public interface IMealRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapCategory")
    @Mapping(target = "restaurant", source = "restaurantId", qualifiedByName = "mapRestaurant")
    Meal addMealRequestToMeal(AddMealRequest addMealRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imageUrl", constant = "Dummy")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "category", constant = "0" , qualifiedByName = "mapCategory")
    @Mapping(target = "restaurant", constant = "0", qualifiedByName = "mapRestaurant")
    Meal updateMealRequestToMeal(UpdateMealRequest updateMealRequest);

    @Named("mapCategory")
    default Category mapCategory(Long categoryId) {
        return new Category(categoryId, "Dummy", "Dummy");
    }

    @Named("mapRestaurant")
    default Restaurant mapRestaurant(Long restaurantId) {
        return new Restaurant(restaurantId, "Dummy", "Dummy", 0L, "000000", "Dummy", "000000");
    }
}
