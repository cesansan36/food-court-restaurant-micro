package plazadecomidas.restaurants.util;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.Authentication;

public interface ITokenUtils {

    String createToken(Authentication authentication);

    DecodedJWT validateToken(String token);
    String extractUsername(DecodedJWT decodedJWT);
    Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName);
}
