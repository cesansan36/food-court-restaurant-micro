package plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception;

public class RegistryAlreadyExistsException extends RuntimeException {

    public RegistryAlreadyExistsException(String message) {
        super(message);
    }
}
