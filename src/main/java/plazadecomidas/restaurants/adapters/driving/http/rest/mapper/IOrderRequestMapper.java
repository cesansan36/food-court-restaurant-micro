package plazadecomidas.restaurants.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.AddOrderRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.MealInOrderRequest;
import plazadecomidas.restaurants.domain.model.Category;
import plazadecomidas.restaurants.domain.model.Meal;
import plazadecomidas.restaurants.domain.model.Order;
import plazadecomidas.restaurants.domain.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clientId", source = "userId")
    @Mapping(target = "date", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "status", source = "orderStatus")
    @Mapping(target = "idChef", ignore = true)
    @Mapping(target = "idRestaurant", source = "addOrderRequest.restaurantId")
    @Mapping(target = "meals", source = "addOrderRequest", qualifiedByName = "mapMeals")
    Order addOrderRequestToOrder(AddOrderRequest addOrderRequest, Long userId, String orderStatus);

    @Named("mapMeals")
    default List<Meal> mapMeals(AddOrderRequest addOrderRequest) {
        List<Meal> meals = new ArrayList<>();

        for (MealInOrderRequest mealInOrd : addOrderRequest.mealsInOrder()) {
            for (int i = 0; i < mealInOrd.quantity(); i++) {
                Meal meal = new Meal(
                        mealInOrd.idMeal(),
                        "Dummy",
                        "Dummy",
                        1L,
                        "Dummy",
                        true,
                        new Restaurant(addOrderRequest.restaurantId(), "Dummy", "Dummy", 0L, "000000", "Dummy", "000000"),
                        new Category(1L, "Dummy", "Dummy")
                );
                meals.add(meal);
            }
        }

        return meals;
    }
}