package plazadecomidas.restaurants.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.response.RestaurantResponse;
import plazadecomidas.restaurants.domain.model.Restaurant;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IRestaurantResponseMapper {

    RestaurantResponse restaurantToRestaurantResponse(Restaurant restaurant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "address", ignore = true)
    List<RestaurantResponse> restaurantsToRestaurantResponses(List<Restaurant> restaurants);
}
