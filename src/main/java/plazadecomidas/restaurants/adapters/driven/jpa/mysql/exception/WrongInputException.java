package plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception;

public class WrongInputException extends RuntimeException{

    public WrongInputException(String message) {
        super(message);
    }
}
