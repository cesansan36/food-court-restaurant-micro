package plazadecomidas.restaurants.domain.primaryport.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plazadecomidas.restaurants.TestData.DomainTestData;
import plazadecomidas.restaurants.domain.exception.ClientHasUnfinishedOrdersException;
import plazadecomidas.restaurants.domain.model.Order;
import plazadecomidas.restaurants.domain.secondaryport.IOrderPersistencePort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderUseCaseTest {

    private OrderUseCase orderUseCase;

    private IOrderPersistencePort orderPersistencePort;

    @BeforeEach
    void setUp() {
        orderPersistencePort = mock(IOrderPersistencePort.class);
        orderUseCase = new OrderUseCase(orderPersistencePort);
    }

    @Test
    void saveOrderCorrectly() {
        Order order = DomainTestData.getValidOrder(1L);

        when(orderPersistencePort.getAmountOfUnfinishedOrders(anyLong())).thenReturn(0);

        orderUseCase.saveOrder(order);

        verify(orderPersistencePort, times(1)).saveOrder(order);
        verify(orderPersistencePort, times(1)).getAmountOfUnfinishedOrders(anyLong());
    }

    @Test
    void saveOrderFailBecauseOfUnfinishedOrders() {
        Order order = DomainTestData.getValidOrder(1L);

        when(orderPersistencePort.getAmountOfUnfinishedOrders(anyLong())).thenReturn(1);

        assertThrows(ClientHasUnfinishedOrdersException.class, () -> orderUseCase.saveOrder(order));

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
}