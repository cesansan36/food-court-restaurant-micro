package plazadecomidas.restaurants.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.response.OrderCancelledResponse;
import plazadecomidas.restaurants.domain.model.OperationResult;

@Mapper(componentModel = "spring")
public interface IOrderCancelledResponseMapper {

    OrderCancelledResponse toCancelledResponse(OperationResult operationResult);
}
