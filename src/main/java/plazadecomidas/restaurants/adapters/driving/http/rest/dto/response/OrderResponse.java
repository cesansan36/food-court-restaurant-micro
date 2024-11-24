package plazadecomidas.restaurants.adapters.driving.http.rest.dto.response;

import java.time.LocalDate;
import java.util.List;

public record OrderResponse(
        Long id,
        Long idClient,
        LocalDate date,
        String status,
        Long idChef,
        Long idRestaurant,
        List<MealInOrderResponse> meals
) {
}
