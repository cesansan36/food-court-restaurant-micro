package plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper;

import org.mapstruct.Mapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import plazadecomidas.restaurants.domain.model.Restaurant;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IRestaurantEntityMapper {

    public RestaurantEntity toEntity(Restaurant restaurant);

    public Restaurant toDomain(RestaurantEntity restaurantEntity);

    public List<Restaurant> toDomainList(List<RestaurantEntity> restaurantEntities);
}
