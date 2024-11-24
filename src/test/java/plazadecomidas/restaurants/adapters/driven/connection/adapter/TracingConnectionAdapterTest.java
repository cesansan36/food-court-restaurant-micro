package plazadecomidas.restaurants.adapters.driven.connection.adapter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import plazadecomidas.restaurants.TestData.DomainTestData;
import plazadecomidas.restaurants.adapters.driven.connection.dto.request.AddRecordRequestFeign;
import plazadecomidas.restaurants.adapters.driven.connection.dto.response.RecordResponseFeign;
import plazadecomidas.restaurants.adapters.driven.connection.feign.ITracingFeignClient;
import plazadecomidas.restaurants.adapters.driven.connection.mapper.IAddRecordRequestFeignMapper;
import plazadecomidas.restaurants.adapters.driven.connection.mapper.IRecordResponseFeignMapper;
import plazadecomidas.restaurants.domain.model.OrderRecord;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TracingConnectionAdapterTest {

    @InjectMocks private TracingConnectionAdapter tracingConnectionAdapter;

    @Mock private ITracingFeignClient tracingFeignClient;
    @Mock private IRecordResponseFeignMapper recordResponseFeignMapper;
    @Mock private IAddRecordRequestFeignMapper recordRequestFeignMapper;

    @Test
    void create() {
        String bearerToken = "token";
        OrderRecord orderRecord = DomainTestData.getValidOrderRecord(1L);
        RecordResponseFeign recordResponseFeign = new RecordResponseFeign(
                1L,
                1L,
                "client@email.com",
                LocalDateTime.now(),
                "PENDING",
                "PREPARING",
                1L,
                "employee@email.com"
        );
        AddRecordRequestFeign addRecordRequestFeign = new AddRecordRequestFeign(
                1L,
                1L,
                "client@email.com",
                "PENDING",
                "PREPARING",
                1L,
                "employee@email.com"
        );

        when(recordRequestFeignMapper.recordToAddRequest(any(OrderRecord.class))).thenReturn(addRecordRequestFeign);
        when(tracingFeignClient.create(anyString(), any(AddRecordRequestFeign.class))).thenReturn(new ResponseEntity<>(recordResponseFeign, HttpStatus.OK));
        when(recordResponseFeignMapper.recordResponseToRecord(any(RecordResponseFeign.class))).thenReturn(DomainTestData.getValidOrderRecord(1L));

        OrderRecord result = tracingConnectionAdapter.create(bearerToken, orderRecord);

        assertAll(
                () -> assertEquals(orderRecord.getClientEmail(), result.getClientEmail()),
                () -> assertEquals(orderRecord.getEmployeeEmail(), result.getEmployeeEmail()),
                () -> assertEquals(orderRecord.getIdOrder(), result.getIdOrder()),
                () -> verify(tracingFeignClient).create(anyString(), any(AddRecordRequestFeign.class)),
                () -> verify(recordResponseFeignMapper).recordResponseToRecord(any(RecordResponseFeign.class))
        );
    }

    @Test
    void list() {
        String bearerToken = "token";
        Long clientId = 1L;
        LocalDateTime now = LocalDateTime.now();
        OrderRecord orderRecord = DomainTestData.getValidOrderRecord(1L);
        RecordResponseFeign recordResponseFeign = new RecordResponseFeign(
                1L,
                1L,
                "client@email.com",
                now,
                "PENDING",
                "PREPARING",
                1L,
                "employee@email.com"
        );

        when(tracingFeignClient.list(anyString(), anyLong())).thenReturn(new ResponseEntity<>(List.of(recordResponseFeign), HttpStatus.OK));
        when(recordResponseFeignMapper.recordResponseToRecord(any(RecordResponseFeign.class))).thenReturn(orderRecord);

        List<OrderRecord> result = tracingConnectionAdapter.list(bearerToken, clientId);

        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals(orderRecord.getClientEmail(), result.getFirst().getClientEmail()),
                () -> assertEquals(orderRecord.getEmployeeEmail(), result.getFirst().getEmployeeEmail()),
                () -> assertEquals(orderRecord.getIdOrder(), result.getFirst().getIdOrder()),
                () -> verify(tracingFeignClient).list(anyString(), anyLong()),
                () -> verify(recordResponseFeignMapper).recordResponseToRecord(any(RecordResponseFeign.class))
        );
    }

    @Test
    void findRecordByOrderIdAndStatus() {
        Long id = 1L;
        String status = "PENDING";
        String token = "token";
        LocalDateTime createdAt = LocalDateTime.now();
        RecordResponseFeign recordResponseFeign = mock(RecordResponseFeign.class);
        OrderRecord orderRecord = DomainTestData.getValidOrderRecord(1L);

        when(tracingFeignClient.findRecordByOrderIdAndStatus(anyString(), anyLong(), anyString())).thenReturn(new ResponseEntity<>(recordResponseFeign, HttpStatus.OK));
        when(recordResponseFeignMapper.recordResponseToRecord(any(RecordResponseFeign.class))).thenReturn(orderRecord);

        OrderRecord result = tracingConnectionAdapter.findRecordByOrderIdAndStatus(id, status, token);

        assertEquals(orderRecord, result);
        verify(tracingFeignClient).findRecordByOrderIdAndStatus(anyString(), anyLong(), anyString());
        verify(recordResponseFeignMapper).recordResponseToRecord(any(RecordResponseFeign.class));

    }
}