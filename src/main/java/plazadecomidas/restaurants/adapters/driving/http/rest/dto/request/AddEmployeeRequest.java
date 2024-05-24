package plazadecomidas.restaurants.adapters.driving.http.rest.dto.request;

public record AddEmployeeRequest(
        Long idOwner,
        Long idEmployee,
        Long idRestaurant
) {
}
