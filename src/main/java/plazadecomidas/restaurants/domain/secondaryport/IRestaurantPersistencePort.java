package plazadecomidas.restaurants.domain.secondaryport;

import plazadecomidas.restaurants.domain.model.Restaurant;

public interface IRestaurantPersistencePort {

    void saveRestaurant(Restaurant restaurant);

    boolean existsRestaurantOwnerPair(Long idRestaurant, Long idOwner);
}
