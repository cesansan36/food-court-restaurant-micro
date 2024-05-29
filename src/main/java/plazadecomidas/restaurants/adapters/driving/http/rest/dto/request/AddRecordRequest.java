package plazadecomidas.restaurants.adapters.driving.http.rest.dto.request;

public record AddRecordRequest(
        Long idOrder,
        Long idClient,
        String clientEmail,
        String previousState,
        String newState,
        Long idEmployee,
        String employeeEmail
) {
}
