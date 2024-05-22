package plazadecomidas.restaurants.adapters.driving.http.rest.dto.request;

public record UpdateMealAvailabilityRequest(
        String name,
        Boolean availability,
        Long restaurantId
) {}
