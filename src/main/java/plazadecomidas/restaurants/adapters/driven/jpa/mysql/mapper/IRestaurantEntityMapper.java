package plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper;

import org.mapstruct.Mapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import plazadecomidas.restaurants.domain.model.Restaurant;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IRestaurantEntityMapper {

    RestaurantEntity toEntity(Restaurant restaurant);

    Restaurant toDomain(RestaurantEntity restaurantEntity);

    List<Restaurant> toDomainList(List<RestaurantEntity> restaurantEntities);
}
