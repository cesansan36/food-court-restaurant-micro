package plazadecomidas.restaurants.domain.primaryport.usecase;

import org.springframework.http.ResponseEntity;
import plazadecomidas.restaurants.adapters.driven.feign.IUserFeignClient;
import plazadecomidas.restaurants.domain.exception.FieldRuleInvalidException;
import plazadecomidas.restaurants.domain.model.Restaurant;
import plazadecomidas.restaurants.domain.primaryport.IRestaurantServicePort;
import plazadecomidas.restaurants.domain.secondaryport.IRestaurantPersistencePort;
import plazadecomidas.restaurants.domain.util.DomainConstants;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserFeignClient userFeignClient;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, IUserFeignClient userFeignClient) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userFeignClient = userFeignClient;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {

        ResponseEntity<Boolean> isUserValid;

        try {
            isUserValid = userFeignClient.verifyRole(restaurant.getOwnerId(), DomainConstants.OWNER_ROLE);
        } catch (Exception e) {
            throw new FieldRuleInvalidException(DomainConstants.USER_ID_NOT_FOUND.formatted(e.getMessage()));
        }

        if (Boolean.FALSE.equals(isUserValid.getBody())) {
            throw new FieldRuleInvalidException(DomainConstants.OWNER_ID_INVALID_MESSAGE);
        }

        restaurantPersistencePort.saveRestaurant(restaurant);
    }
}
