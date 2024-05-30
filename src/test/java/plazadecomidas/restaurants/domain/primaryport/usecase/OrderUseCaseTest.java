package plazadecomidas.restaurants.domain.primaryport.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import plazadecomidas.restaurants.TestData.DomainTestData;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryNotFoundException;
import plazadecomidas.restaurants.domain.exception.ClientHasUnfinishedOrdersException;
import plazadecomidas.restaurants.domain.model.OperationResult;
import plazadecomidas.restaurants.domain.model.Order;
import plazadecomidas.restaurants.domain.primaryport.IOrderRecordPrimaryPort;
import plazadecomidas.restaurants.domain.secondaryport.IOrderMessagingPort;
import plazadecomidas.restaurants.domain.secondaryport.IOrderPersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IUserConnectionPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @InjectMocks private OrderUseCase orderUseCase;

    @Mock private IOrderPersistencePort orderPersistencePort;
    @Mock private IUserConnectionPort userConnectionPort;
    @Mock private IOrderMessagingPort orderMessagingPort;
    @Mock private IOrderRecordPrimaryPort orderRecordPrimaryPort;

    @Test
    void saveOrderCorrectly() {
        Order order = DomainTestData.getValidOrder(1L);
        String bearerToken = "bearerToken";

        when(orderPersistencePort.getAmountOfUnfinishedOrders(anyLong())).thenReturn(0);

        orderUseCase.saveOrder(order, bearerToken);

        verify(orderPersistencePort, times(1)).saveOrder(any(Order.class));
        verify(orderPersistencePort, times(1)).getAmountOfUnfinishedOrders(anyLong());
    }

    @Test
    void saveOrderFailBecauseOfUnfinishedOrders() {
        Order order = DomainTestData.getValidOrder(1L);
        String bearerToken = "bearerToken";

        when(orderPersistencePort.getAmountOfUnfinishedOrders(anyLong())).thenReturn(1);

        assertThrows(ClientHasUnfinishedOrdersException.class, () -> orderUseCase.saveOrder(order, bearerToken));

        verify(orderPersistencePort, times(0)).saveOrder(order);
        verify(orderPersistencePort, times(1)).getAmountOfUnfinishedOrders(anyLong());
    }

    @Test
    void getOrdersByStatus() {
        Order order = DomainTestData.getValidOrder(1L);

        when(orderPersistencePort.getOrdersByStatus(anyLong(), anyInt(), anyInt(), anyString())).thenReturn(List.of(order));

        List<Order> orders = orderUseCase.getOrdersByStatus(1L, 0, 10, "PENDING");

        verify(orderPersistencePort, times(1)).getOrdersByStatus(anyLong(), anyInt(), anyInt(), anyString());
        assertEquals(List.of(order), orders);
        assertEquals(1, orders.size());

    }

    @Test
    void updateOrderPreparing() {
        Order order = DomainTestData.getValidOrder(1L);
        String bearerToken = "bearerToken";

        orderUseCase.updateOrderPreparing(order, bearerToken);

        verify(orderPersistencePort, times(1)).updateOrderPreparing(order);
    }

    @Test
    void updateOrderReadySuccess() {
        Order order = DomainTestData.getValidOrder(1L);
        String token = "token";
        String phoneNumber = "123456789";

        when(orderPersistencePort.updateOrderReady(order)).thenReturn(order);
        when(userConnectionPort.getUserPhoneNumber(anyLong(), anyString())).thenReturn(phoneNumber);

        orderUseCase.updateOrderReady(order, token);

        verify(orderPersistencePort, times(1)).updateOrderReady(order);
        verify(orderMessagingPort, times(1)).sendOrderReadyMessage(token, order, phoneNumber);
    }

    @Test
    void updateOrderReadyFailOnUserNotFound() {
        Order order = DomainTestData.getValidOrder(1L);
        String token = "token";
        String phoneNumber = "123456789";

        when(orderPersistencePort.updateOrderReady(order)).thenReturn(order);
        doThrow(new RuntimeException()).when(userConnectionPort).getUserPhoneNumber(anyLong(), anyString());

        assertThrows(RegistryNotFoundException.class, () -> orderUseCase.updateOrderReady(order, token));

        verify(orderPersistencePort, times(1)).updateOrderReady(order);
        verify(orderMessagingPort, times(0)).sendOrderReadyMessage(token, order, phoneNumber);
    }

    @Test
    void updateOrderDelivered() {
        Order order = DomainTestData.getValidOrder(1L);
        String bearerToken = "bearerToken";

        orderUseCase.updateOrderDelivered(order, bearerToken);

        verify(orderPersistencePort, times(1)).updateOrderDelivered(order);
    }

    @Test
    void updateOrderCancelledSuccess(){
        boolean result = true;
        String message = "The order was cancelled successfully";
        Order order = mock(Order.class);
        String bearerToken = "bearerToken";

        when(orderPersistencePort.tryCancelOrder(order)).thenReturn(result);

        OperationResult operationResult = orderUseCase.updateOrderCancelled(order, bearerToken);

        assertEquals(result, operationResult.isSuccess());
        assertEquals(message, operationResult.getMessage());
    }

    @Test
    void updateOrderCancelledFail(){
        boolean result = false;
        String message = "Lo sentimos, tu pedido ya está en preparación y no puede cancelarse";
        Order order = mock(Order.class);
        String bearerToken = "bearerToken";

        when(orderPersistencePort.tryCancelOrder(order)).thenReturn(result);

        OperationResult operationResult = orderUseCase.updateOrderCancelled(order, bearerToken);

        assertEquals(result, operationResult.isSuccess());
        assertEquals(message, operationResult.getMessage());
    }
}