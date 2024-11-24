package plazadecomidas.restaurants.domain.primaryport;

import plazadecomidas.restaurants.domain.model.Restaurant;

import java.util.List;

public interface IRestaurantServicePort {

    void saveRestaurant(Restaurant restaurant);

    List<Restaurant> getAllRestaurants(Integer page, Integer size);
}
