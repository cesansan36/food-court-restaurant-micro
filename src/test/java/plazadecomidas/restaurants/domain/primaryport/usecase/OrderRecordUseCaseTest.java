package plazadecomidas.restaurants.domain.primaryport.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import plazadecomidas.restaurants.TestData.DomainTestData;
import plazadecomidas.restaurants.domain.model.Order;
import plazadecomidas.restaurants.domain.model.OrderRecord;
import plazadecomidas.restaurants.domain.secondaryport.ITracingConnectionPort;
import plazadecomidas.restaurants.domain.secondaryport.IUserConnectionPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderRecordUseCaseTest {

    @InjectMocks private OrderRecordUseCase orderRecordUseCase;

    @Mock private ITracingConnectionPort tracingConnectionPort;
    @Mock private IUserConnectionPort userConnectionPort;

    @Test
    void listRecords() {
        String Token = "Token";
        Long clientId = 1L;

        OrderRecord orderRecord = DomainTestData.getValidOrderRecord(1L);

        when(tracingConnectionPort.list(Token, clientId)).thenReturn(List.of(orderRecord));

        List<OrderRecord> result = orderRecordUseCase.listRecords(Token, clientId);

        assertEquals(List.of(orderRecord), result);

        verify(tracingConnectionPort, times(1)).list(Token, clientId);
    }

    @Test
    void createOrderRecord() {
        Order order = DomainTestData.getValidOrder(1L);
        String Token = "Token";
        String email = "email@example.com";
        OrderRecord orderRecord = DomainTestData.getValidOrderRecord(1L);

        when(userConnectionPort.getUserEmail(Token)).thenReturn(email);

        orderRecordUseCase.createOrderRecord(order, Token);

        verify(tracingConnectionPort, times(1)).create(anyString(), any(OrderRecord.class));
        verify(userConnectionPort, times(1)).getUserEmail(Token);
    }

    @Test
    void assignChefToOrder() {
        Order order = DomainTestData.getValidOrder(1L);
        String token = "token";
        String userEmail = "email@example.com";
        OrderRecord orderRecord = DomainTestData.getValidOrderRecord(1L);

        when(userConnectionPort.getUserEmail(token)).thenReturn(userEmail);
        when(tracingConnectionPort.findRecordByOrderIdAndStatus(anyLong(), anyString(), anyString())).thenReturn(orderRecord);

        orderRecordUseCase.assignChefToOrder(order, token);

        verify(tracingConnectionPort, times(1)).findRecordByOrderIdAndStatus(anyLong(), anyString(), anyString());
        verify(tracingConnectionPort, times(1)).create(anyString(), any(OrderRecord.class));
    }

    @Test
    void updateOrderToReady() {
        Order order = DomainTestData.getValidOrder(1L);
        String token = "token";
        OrderRecord orderRecord = DomainTestData.getValidOrderRecord(1L);

        when(tracingConnectionPort.findRecordByOrderIdAndStatus(anyLong(), anyString(), anyString())).thenReturn(orderRecord);

        orderRecordUseCase.updateOrderToReady(order, token);

        verify(tracingConnectionPort, times(1)).findRecordByOrderIdAndStatus(anyLong(), anyString(), anyString());
        verify(tracingConnectionPort, times(1)).create(anyString(), any(OrderRecord.class));
    }

    @Test
    void updateOrderToDelivered() {
        Order order = DomainTestData.getValidOrder(1L);
        String token = "token";
        OrderRecord orderRecord = DomainTestData.getValidOrderRecord(1L);

        when(tracingConnectionPort.findRecordByOrderIdAndStatus(anyLong(), anyString(), anyString())).thenReturn(orderRecord);

        orderRecordUseCase.updateOrderToDelivered(order, token);

        verify(tracingConnectionPort, times(1)).findRecordByOrderIdAndStatus(anyLong(), anyString(), anyString());
        verify(tracingConnectionPort, times(1)).create(anyString(), any(OrderRecord.class));
    }

    @Test
    void updateOrderToCancelled() {
        Order order = DomainTestData.getValidOrder(1L);
        String token = "token";
        OrderRecord orderRecord = DomainTestData.getValidOrderRecord(1L);

        when(tracingConnectionPort.findRecordByOrderIdAndStatus(anyLong(), anyString(), anyString())).thenReturn(orderRecord);

        orderRecordUseCase.updateOrderToCancelled(order, token);

        verify(tracingConnectionPort, times(1)).findRecordByOrderIdAndStatus(anyLong(), anyString(), anyString());
        verify(tracingConnectionPort, times(1)).create(anyString(), any(OrderRecord.class));
    }
}