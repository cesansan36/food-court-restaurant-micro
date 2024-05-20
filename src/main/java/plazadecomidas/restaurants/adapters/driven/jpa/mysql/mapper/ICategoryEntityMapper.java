package plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper;

import org.mapstruct.Mapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.CategoryEntity;
import plazadecomidas.restaurants.domain.model.Category;

@Mapper(componentModel = "spring")
public interface ICategoryEntityMapper {

    Category entityToDomain(CategoryEntity entity);
}
