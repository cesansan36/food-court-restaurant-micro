package plazadecomidas.restaurants.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.AddOrderRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.MealInOrderRequest;
import plazadecomidas.restaurants.domain.model.MealOrder;
import plazadecomidas.restaurants.domain.model.Order;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idClient", source = "userId")
    @Mapping(target = "date", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "status", source = "orderStatus")
    @Mapping(target = "idChef", ignore = true)
    @Mapping(target = "idRestaurant", source = "addOrderRequest.restaurantId")
    @Mapping(target = "meals", source = "addOrderRequest", qualifiedByName = "mapMeals")
    Order addOrderRequestToOrder(AddOrderRequest addOrderRequest, Long userId, String orderStatus);

    @Named("mapMeals")
    default List<MealOrder> mapMeals(AddOrderRequest addOrderRequest) {
        List<MealOrder> meals = new ArrayList<>();

        for (MealInOrderRequest meal : addOrderRequest.mealsInOrder()) {
            MealOrder mealOrder = new MealOrder(meal.idMeal(), null, meal.quantity());
            meals.add(mealOrder);
        }

        return meals;
    }
}