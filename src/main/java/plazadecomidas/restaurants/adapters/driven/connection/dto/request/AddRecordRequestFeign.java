package plazadecomidas.restaurants.adapters.driven.connection.dto.request;

public record AddRecordRequestFeign(
        Long idOrder,
        Long idClient,
        String clientEmail,
        String previousState,
        String newState,
        Long idEmployee,
        String employeeEmail
) {
}
