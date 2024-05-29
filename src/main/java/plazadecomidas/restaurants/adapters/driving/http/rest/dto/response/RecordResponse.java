package plazadecomidas.restaurants.adapters.driving.http.rest.dto.response;

import java.time.LocalDateTime;

public record RecordResponse(
    Long idOrder,
    Long idClient,
    String clientEmail,
    LocalDateTime createdAt,
    String previousState,
    String newState,
    Long idEmployee,
    String employeeEmail
){
}
