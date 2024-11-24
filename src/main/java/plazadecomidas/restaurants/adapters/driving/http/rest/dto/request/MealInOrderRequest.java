package plazadecomidas.restaurants.adapters.driving.http.rest.dto.request;

public record MealInOrderRequest(
        Long idMeal,
        Integer quantity
) {
}
