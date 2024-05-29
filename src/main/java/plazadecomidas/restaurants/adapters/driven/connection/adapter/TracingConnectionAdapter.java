package plazadecomidas.restaurants.adapters.driven.connection.adapter;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import plazadecomidas.restaurants.adapters.driven.connection.dto.request.AddRecordRequestFeign;
import plazadecomidas.restaurants.adapters.driven.connection.dto.response.RecordResponseFeign;
import plazadecomidas.restaurants.adapters.driven.connection.feign.ITracingFeignClient;
import plazadecomidas.restaurants.adapters.driven.connection.mapper.IAddRecordRequestFeignMapper;
import plazadecomidas.restaurants.adapters.driven.connection.mapper.IRecordResponseFeignMapper;
import plazadecomidas.restaurants.domain.model.OrderRecord;
import plazadecomidas.restaurants.domain.secondaryport.ITracingConnectionPort;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class TracingConnectionAdapter implements ITracingConnectionPort {

    private final ITracingFeignClient tracingFeignClient;
    private final IRecordResponseFeignMapper recordResponseFeignMapper;
    private final IAddRecordRequestFeignMapper recordRequestFeignMapper;

    @Override
    public OrderRecord create(String token, OrderRecord request) {

        AddRecordRequestFeign addRecordRequestFeign = recordRequestFeignMapper.recordToAddRequest(request);

        ResponseEntity<RecordResponseFeign> response = tracingFeignClient.create(token, addRecordRequestFeign);

        return recordResponseFeignMapper.recordResponseToRecord(response.getBody());
    }

    @Override
    public List<OrderRecord> list(String token, Long clientId) {
        ResponseEntity<List<RecordResponseFeign>> response = tracingFeignClient.list(token, clientId);
        return Objects.requireNonNull(response.getBody()).stream().map(recordResponseFeignMapper::recordResponseToRecord).toList();
    }

    @Override
    public OrderRecord findRecordByOrderIdAndStatus(Long id, String name, String token) {
        return recordResponseFeignMapper.recordResponseToRecord(
                tracingFeignClient.findRecordByOrderIdAndStatus(token, id, name).getBody());
    }
}
