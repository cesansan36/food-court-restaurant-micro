package plazadecomidas.restaurants.adapters.driven.connection.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import plazadecomidas.restaurants.adapters.driven.connection.dto.SendMessageRequest;
import plazadecomidas.restaurants.domain.model.Order;

@Mapper(componentModel = "spring")
public interface ISendMessageRequestMapper {

    @Mapping(target = "to", source = "phoneNumber")
    @Mapping(target = "message", source = "order", qualifiedByName = "mapMessage")
    SendMessageRequest toSendMessageRequest(Order order, String phoneNumber);

    @Named("mapMessage")
    default String mapMessage(Order order) {

        return "Your order with id " + order.getId() + " is ready. This is your security pin: " + order.getSecurityPin();
    }
}
