package plazadecomidas.restaurants.adapters.driving.http.rest.dto.request;

public record UpdateOrderToDeliveredRequest(
        Long id,
        Integer securityPin
) {
}
