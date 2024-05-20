package plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper;

import org.mapstruct.Mapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.MealEntity;
import plazadecomidas.restaurants.domain.model.Meal;

@Mapper(componentModel = "spring")
public interface IMealEntityMapper {

    MealEntity toEntity(Meal meal);
}
