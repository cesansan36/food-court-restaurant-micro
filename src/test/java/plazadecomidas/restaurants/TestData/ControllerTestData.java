package plazadecomidas.restaurants.TestData;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ControllerTestData {

    private ControllerTestData() {
        throw new IllegalStateException("Utility class");
    }

    public static Claim getIdClaim (Long id) {
        return new Claim() {
            @Override
            public boolean isNull() {
                return false;
            }

            @Override
            public boolean isMissing() {
                return false;
            }

            @Override
            public Boolean asBoolean() {
                return null;
            }

            @Override
            public Integer asInt() {
                return 0;
            }

            @Override
            public Long asLong() {
                return id;
            }

            @Override
            public Double asDouble() {
                return 0.0;
            }

            @Override
            public String asString() {
                return "";
            }

            @Override
            public Date asDate() {
                return null;
            }

            @Override
            public <T> T[] asArray(Class<T> aClass) throws JWTDecodeException {
                return null;
            }

            @Override
            public <T> List<T> asList(Class<T> aClass) throws JWTDecodeException {
                return List.of();
            }

            @Override
            public Map<String, Object> asMap() throws JWTDecodeException {
                return Map.of();
            }

            @Override
            public <T> T as(Class<T> aClass) throws JWTDecodeException {
                return null;
            }
        };
    }
}
