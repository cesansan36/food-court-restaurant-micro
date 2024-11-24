package plazadecomidas.restaurants.adapters.driven.connection.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import plazadecomidas.restaurants.adapters.driven.connection.dto.request.AddRecordRequestFeign;
import plazadecomidas.restaurants.domain.model.OrderRecord;


@Mapper(componentModel = "spring")
public interface IAddRecordRequestFeignMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    OrderRecord addRequestToRecord(AddRecordRequestFeign request);

    AddRecordRequestFeign recordToAddRequest(OrderRecord orderRecord);
}
