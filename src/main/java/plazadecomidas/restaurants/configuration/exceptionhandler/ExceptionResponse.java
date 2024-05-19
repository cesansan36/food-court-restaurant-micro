package plazadecomidas.restaurants.configuration.exceptionhandler;

import java.time.LocalDateTime;

public class ExceptionResponse {

    private final String message;
    private final String status;
    private final LocalDateTime timestamp;

    public ExceptionResponse(String message, String status, LocalDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
