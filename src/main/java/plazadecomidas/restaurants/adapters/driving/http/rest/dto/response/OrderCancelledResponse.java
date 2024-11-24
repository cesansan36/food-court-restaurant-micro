package plazadecomidas.restaurants.adapters.driving.http.rest.dto.response;

public record OrderCancelledResponse(
    boolean success,
    String message
) {
}
