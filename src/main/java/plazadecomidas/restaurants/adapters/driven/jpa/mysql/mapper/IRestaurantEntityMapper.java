package plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper;

import org.mapstruct.Mapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import plazadecomidas.restaurants.domain.model.Restaurant;

@Mapper(componentModel = "spring")
public interface IRestaurantEntityMapper {

    public RestaurantEntity toEntity(Restaurant restaurant);
}
