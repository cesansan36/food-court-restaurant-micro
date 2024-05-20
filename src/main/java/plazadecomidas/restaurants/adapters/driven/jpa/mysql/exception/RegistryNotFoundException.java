package plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception;

public class RegistryNotFoundException extends RuntimeException{

    public RegistryNotFoundException(String message) {
        super(message);
    }
}
