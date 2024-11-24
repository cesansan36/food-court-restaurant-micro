package plazadecomidas.restaurants.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.response.MealInOrderResponse;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.response.OrderResponse;
import plazadecomidas.restaurants.domain.model.MealOrder;
import plazadecomidas.restaurants.domain.model.Order;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderResponseMapper {

    @Mapping(source = "meals", target = "meals", qualifiedByName = "mapMeals")
    List<OrderResponse> ordersToOrderResponses(List<Order> ordersByStatus);

    @Named("mapMeals")
    default List<MealInOrderResponse> mapMeals(List<MealOrder> meals) {
        List<MealInOrderResponse> orderMeals = new ArrayList<>();

        for (MealOrder meal : meals) {
            MealInOrderResponse orderMeal = new MealInOrderResponse(
                    meal.getIdMeal(),
                    meal.getQuantity()
            );
            orderMeals.add(orderMeal);
        }
        return orderMeals;
    }
}
