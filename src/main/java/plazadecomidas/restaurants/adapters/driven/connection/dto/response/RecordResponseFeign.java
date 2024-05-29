package plazadecomidas.restaurants.adapters.driven.connection.dto.response;

import java.time.LocalDateTime;

public record RecordResponseFeign(
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
