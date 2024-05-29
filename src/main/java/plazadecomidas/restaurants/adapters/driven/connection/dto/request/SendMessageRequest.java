package plazadecomidas.restaurants.adapters.driven.connection.dto.request;

public record SendMessageRequest(
    String to,
    String message
) {
}
