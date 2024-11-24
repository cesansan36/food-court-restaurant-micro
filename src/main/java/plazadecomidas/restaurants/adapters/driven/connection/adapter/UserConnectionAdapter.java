package plazadecomidas.restaurants.adapters.driven.connection.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import plazadecomidas.restaurants.adapters.driven.connection.feign.IUserFeignClient;
import plazadecomidas.restaurants.domain.secondaryport.IUserConnectionPort;

@RequiredArgsConstructor
public class UserConnectionAdapter implements IUserConnectionPort {

    private final IUserFeignClient userFeignClient;

    @Override
    public boolean verifyRole(Long idClient, String role) {
        ResponseEntity<Boolean> response = userFeignClient.verifyRole(idClient, role);
        return Boolean.TRUE.equals(response.getBody());
    }

    @Override
    public String getUserPhoneNumber(Long idClient, String token) {
        ResponseEntity<String> response = userFeignClient.getNumber(token, idClient);
        return response.getBody();
    }

    @Override
    public String getUserEmail(String token) {
        ResponseEntity<String> response = userFeignClient.getEmail(token);
        return response.getBody();
    }
}
