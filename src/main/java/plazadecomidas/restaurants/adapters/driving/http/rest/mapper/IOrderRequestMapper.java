package plazadecomidas.restaurants.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.AddOrderRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.MealInOrderRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.UpdateOrderRequest;
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
    @Mapping(target = "securityPin", constant = "0")
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

    @Mapping(target = "id", source = "request.id")
    @Mapping(target = "idClient", constant = "0L")
    @Mapping(target = "idChef", source = "userId")
    @Mapping(target = "idRestaurant", constant = "0L")
    @Mapping(target = "meals", source = "request", qualifiedByName = "dummyMealsList")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "securityPin", constant = "0")
    @Mapping(target = "date", expression = "java(java.time.LocalDate.now())")
    Order updateOrderRequestToOrder(UpdateOrderRequest request, Long userId, String status);

    @Named("dummyMealsList")
    default List<MealOrder> dummyMealsList(UpdateOrderRequest request) {
        return new ArrayList<>();
    }
}