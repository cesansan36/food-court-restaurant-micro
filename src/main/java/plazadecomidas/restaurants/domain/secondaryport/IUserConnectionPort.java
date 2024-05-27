package plazadecomidas.restaurants.domain.secondaryport;

public interface IUserConnectionPort {

    boolean verifyRole(Long idClient, String role);

    String getUserPhoneNumber(Long idClient, String token);
}
