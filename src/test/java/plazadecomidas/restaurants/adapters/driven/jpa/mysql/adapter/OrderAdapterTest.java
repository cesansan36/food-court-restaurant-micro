package plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plazadecomidas.restaurants.TestData.PersistenceTestData;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.OrderEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.OrderMealEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryMismatchException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IOrderEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IMealRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IOrderRepository;
import plazadecomidas.restaurants.domain.model.Order;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderAdapterTest {

    private OrderAdapter orderAdapter;

    private IOrderRepository orderRepository;
    private IMealRepository mealRepository;
    private IOrderEntityMapper orderEntityMapper;

    @BeforeEach
    void setUp() {
        orderRepository = mock(IOrderRepository.class);
        mealRepository = mock(IMealRepository.class);
        orderEntityMapper = mock(IOrderEntityMapper.class);
        orderAdapter = new OrderAdapter(orderRepository, mealRepository, orderEntityMapper);
    }

    @Test
    void getAmountOfUnfinishedOrders() {
        Long clientId = 1L;
        int amount = 3;

        when(orderRepository.countPendingOrPreparingOrReadyOrdersByClientId(anyLong())).thenReturn(amount);

        int amountPending = orderAdapter.getAmountOfUnfinishedOrders(clientId);
        assertEquals(amount, amountPending);
    }

    @Test
    void saveOrderSuccessfully() {
        Order order = mock(Order.class);
        OrderEntity orderEntity = PersistenceTestData.getValidOrderEntity(1L);
        OrderMealEntity orderMealEntity = PersistenceTestData.getValidOrderMealEntity(1L);
        List<Long> restaurantIds = List.of(1L);

        when(orderEntityMapper.orderToOrderEntity(any(Order.class))).thenReturn(orderEntity);
        when(mealRepository.findRestaurantIdsByMealIds(anyList())).thenReturn(restaurantIds);

        orderAdapter.saveOrder(order);

        verify(orderRepository, times(1)).save(any(OrderEntity.class));
        verify(mealRepository, times(1)).findRestaurantIdsByMealIds(anyList());
        verify(orderEntityMapper, times(1)).orderToOrderEntity(any(Order.class));
    }

    @Test
    void saveOrderFailBecauseRestaurantsMismatch() {
        Order order = mock(Order.class);
        OrderEntity orderEntity = PersistenceTestData.getValidOrderEntity(1L);
        OrderMealEntity orderMealEntity = PersistenceTestData.getValidOrderMealEntity(1L);
        List<Long> restaurantIds = List.of(1L, 2L);

        when(orderEntityMapper.orderToOrderEntity(any(Order.class))).thenReturn(orderEntity);
        when(mealRepository.findRestaurantIdsByMealIds(anyList())).thenReturn(restaurantIds);

        assertThrows(RegistryMismatchException.class, () -> orderAdapter.saveOrder(order));

        verify(orderRepository, times(0)).save(any(OrderEntity.class));
        verify(mealRepository, times(1)).findRestaurantIdsByMealIds(anyList());
        verify(orderEntityMapper, times(1)).orderToOrderEntity(any(Order.class));
    }
}