package plazadecomidas.restaurants.domain.secondaryport;

import plazadecomidas.restaurants.domain.model.Restaurant;

import java.util.List;

public interface IRestaurantPersistencePort {

    void saveRestaurant(Restaurant restaurant);

    boolean existsRestaurantOwnerPair(Long idRestaurant, Long idOwner);

    List<Restaurant> getAllRestaurants(Integer page, Integer size);

    boolean existsOwnerRestaurantMatch(Long idOwner, Long idRestaurant);
}
