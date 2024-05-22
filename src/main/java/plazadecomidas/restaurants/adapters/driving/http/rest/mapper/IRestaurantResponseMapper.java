package plazadecomidas.restaurants.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.response.RestaurantResponse;
import plazadecomidas.restaurants.domain.model.Restaurant;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IRestaurantResponseMapper {

    RestaurantResponse restaurantToRestaurantResponse(Restaurant restaurant);

    List<RestaurantResponse> restaurantsToRestaurantResponses(List<Restaurant> restaurants);
}
