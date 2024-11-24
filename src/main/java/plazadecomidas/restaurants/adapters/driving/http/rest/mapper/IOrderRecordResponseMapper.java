package plazadecomidas.restaurants.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.response.RecordResponse;
import plazadecomidas.restaurants.domain.model.OrderRecord;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderRecordResponseMapper {

    RecordResponse toRecordResponse(OrderRecord orderRecord);

    List<RecordResponse> recordsToRecordResponses(List<OrderRecord> orderRecords);
}
