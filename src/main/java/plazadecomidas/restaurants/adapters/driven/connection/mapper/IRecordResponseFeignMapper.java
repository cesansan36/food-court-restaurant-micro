package plazadecomidas.restaurants.adapters.driven.connection.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import plazadecomidas.restaurants.adapters.driven.connection.dto.response.RecordResponseFeign;
import plazadecomidas.restaurants.domain.model.OrderRecord;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IRecordResponseFeignMapper {

    RecordResponseFeign recordToRecordResponse(OrderRecord orderRecord);

    List<RecordResponseFeign> recordListToRecordResponseList(List<OrderRecord> byClientIdOrderByCreatedAtDesc);

    @Mapping(target = "id", ignore = true)
    OrderRecord recordResponseToRecord(RecordResponseFeign recordResponseFeign);

    List<OrderRecord> recordResponseListToRecordList(List<RecordResponseFeign> recordResponseFeignList);
}
