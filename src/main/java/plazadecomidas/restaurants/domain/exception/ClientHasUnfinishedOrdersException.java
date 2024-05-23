package plazadecomidas.restaurants.domain.exception;

public class ClientHasUnfinishedOrdersException extends RuntimeException{

    public ClientHasUnfinishedOrdersException(String message) {
        super(message);
    }
}
