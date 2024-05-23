package plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper;

import org.mapstruct.Mapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.OrderEntity;
import plazadecomidas.restaurants.domain.model.Order;

@Mapper(componentModel = "spring")
public interface IOrderEntityMapper {

    OrderEntity orderToOrderEntity(Order order);
}
