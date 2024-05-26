package plazadecomidas.restaurants.domain.primaryport.usecase;

import plazadecomidas.restaurants.domain.exception.FieldRuleInvalidException;
import plazadecomidas.restaurants.domain.model.Restaurant;
import plazadecomidas.restaurants.domain.primaryport.IRestaurantServicePort;
import plazadecomidas.restaurants.domain.secondaryport.IRestaurantPersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IUserConnectionPort;
import plazadecomidas.restaurants.domain.util.DomainConstants;

import java.util.List;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserConnectionPort userConnectionPort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, IUserConnectionPort userConnectionPort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userConnectionPort = userConnectionPort;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {

        boolean isUserValid;
        try {
            isUserValid = userConnectionPort.verifyRole(restaurant.getOwnerId(), DomainConstants.OWNER_ROLE);
        } catch (Exception e) {
            throw new FieldRuleInvalidException(DomainConstants.USER_ID_NOT_FOUND_MESSAGE.formatted(e.getMessage()));
        }

        if (!isUserValid) {
            throw new FieldRuleInvalidException(DomainConstants.OWNER_ID_INVALID_MESSAGE);
        }

        restaurantPersistencePort.saveRestaurant(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantPersistencePort.getAllRestaurants();
    }
}
