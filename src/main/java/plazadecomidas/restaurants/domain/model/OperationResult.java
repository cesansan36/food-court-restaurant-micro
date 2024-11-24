package plazadecomidas.restaurants.domain.model;

public class OperationResult {

    private final boolean success;
    private final String message;

    public OperationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
