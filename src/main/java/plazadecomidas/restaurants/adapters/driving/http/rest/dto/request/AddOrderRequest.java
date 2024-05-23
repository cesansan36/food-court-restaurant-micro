package plazadecomidas.restaurants.adapters.driving.http.rest.dto.request;

import java.util.List;

public record AddOrderRequest(
    Long restaurantId,
    List<MealInOrderRequest> mealsInOrder
) {
}
