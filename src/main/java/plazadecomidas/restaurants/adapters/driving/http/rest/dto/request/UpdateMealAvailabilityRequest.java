package plazadecomidas.restaurants.adapters.driving.http.rest.dto.request;

public record UpdateMealAvailabilityRequest(
        Long idMeal,
        Boolean availability
) {}
