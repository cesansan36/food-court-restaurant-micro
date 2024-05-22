package plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.MealEntity;
import plazadecomidas.restaurants.domain.model.Meal;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IMealEntityMapper {

    MealEntity toEntity(Meal meal);

    @Mapping(target = "isActive", source = "active")
    Meal toDomain(MealEntity mealEntity);

    List<Meal> toDomainList(List<MealEntity> mealEntityList);
}
