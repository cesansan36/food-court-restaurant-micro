package plazadecomidas.restaurants.domain.secondaryport;

public interface IUserConnectionPort {

    boolean verifyRole(Long idClient, String role);

    public String getUserPhoneNumber(Long idClient, String token);
}
