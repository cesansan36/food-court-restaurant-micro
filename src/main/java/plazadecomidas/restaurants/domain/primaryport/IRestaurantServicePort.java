package plazadecomidas.restaurants.domain.primaryport;

import plazadecomidas.restaurants.domain.model.Restaurant;

public interface IRestaurantServicePort {

    void saveRestaurant(Restaurant restaurant);
}
