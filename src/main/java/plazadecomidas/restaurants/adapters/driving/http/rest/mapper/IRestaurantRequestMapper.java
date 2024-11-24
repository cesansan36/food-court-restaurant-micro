package plazadecomidas.restaurants.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.AddRestaurantRequest;
import plazadecomidas.restaurants.domain.model.Restaurant;

@Mapper(componentModel = "spring")
public interface IRestaurantRequestMapper {

    @Mapping(target = "id", ignore = true)
    Restaurant addRestaurantRequestToRestaurant(AddRestaurantRequest addRestaurantRequest);
}
