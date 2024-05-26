package plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import plazadecomidas.restaurants.TestData.DomainTestData;
import plazadecomidas.restaurants.TestData.PersistenceTestData;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.OrderEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.OrderMealEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryMismatchException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryNotFoundException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.WrongConditionsException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.WrongInputException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IOrderEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IMealRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IOrderRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.util.PersistenceConstants;
import plazadecomidas.restaurants.domain.model.Order;
import plazadecomidas.restaurants.domain.secondaryport.IEmployeePersistencePort;
import plazadecomidas.restaurants.domain.util.DomainConstants;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
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
    private IEmployeePersistencePort employeePersistencePort;

    @BeforeEach
    void setUp() {
        orderRepository = mock(IOrderRepository.class);
        mealRepository = mock(IMealRepository.class);
        orderEntityMapper = mock(IOrderEntityMapper.class);
        employeePersistencePort = mock(IEmployeePersistencePort.class);
        orderAdapter = new OrderAdapter(orderRepository, mealRepository, orderEntityMapper, employeePersistencePort);
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

    @Test
    void getOrdersByStatusAll() {
        Long userId = 1L;
        int page = 1;
        int size = 10;
        String status = PersistenceConstants.ALL_STATUS_FILTER;
        OrderEntity orderEntity = PersistenceTestData.getValidOrderEntity(1L);
        Order order = DomainTestData.getValidOrder(1L);

        when(employeePersistencePort.getRestaurantIdByEmployeeId(anyLong())).thenReturn(1L);
        when(orderRepository.findAllByRestaurantId(any(Pageable.class), anyLong())).thenReturn(new PageImpl<>(List.of(orderEntity)));
        when(orderEntityMapper.orderEntitiesToOrders(anyList())).thenReturn(List.of(order));

        List<Order> orders = orderAdapter.getOrdersByStatus(userId, page, size, status);

        assertAll(
            () -> assertEquals(List.of(order), orders),
            () -> verify(orderRepository, times(1)).findAllByRestaurantId(any(Pageable.class), anyLong()),
            () -> verify(orderEntityMapper, times(1)).orderEntitiesToOrders(anyList()),
            () -> verify(employeePersistencePort, times(1)).getRestaurantIdByEmployeeId(anyLong()),
            () -> verify(orderRepository, times(0)).findAllByRestaurantIdAndStatus(any(Pageable.class), anyLong(), any(String.class))
        );
    }

    @ParameterizedTest
    @EnumSource(DomainConstants.OrderStatus.class)
    void getOrdersByStatusFiltered(DomainConstants.OrderStatus status) {
        Long userId = 1L;
        int page = 1;
        int size = 10;
        OrderEntity orderEntity = PersistenceTestData.getValidOrderEntity(1L);
        Order order = DomainTestData.getValidOrder(1L);

        when(employeePersistencePort.getRestaurantIdByEmployeeId(anyLong())).thenReturn(1L);
        when(orderRepository.findAllByRestaurantIdAndStatus(any(Pageable.class), anyLong(), any(String.class))).thenReturn(new PageImpl<>(List.of(orderEntity)));
        when(orderEntityMapper.orderEntitiesToOrders(anyList())).thenReturn(List.of(order));

        List<Order> orders = orderAdapter.getOrdersByStatus(userId, page, size, status.name());

        assertAll(
                () -> assertEquals(List.of(order), orders),
                () -> verify(orderRepository, times(1)).findAllByRestaurantIdAndStatus(any(Pageable.class), anyLong(), any(String.class)),
                () -> verify(orderEntityMapper, times(1)).orderEntitiesToOrders(anyList()),
                () -> verify(employeePersistencePort, times(1)).getRestaurantIdByEmployeeId(anyLong()),
                () -> verify(orderRepository, times(0)).findAllByRestaurantId(any(Pageable.class), anyLong())
        );
    }

    @Test
    void getOrdersByStatusExceptionByWrongStatusFilter() {
        Long userId = 1L;
        int page = 1;
        int size = 10;
        String status = "wrong status filter";

        when(employeePersistencePort.getRestaurantIdByEmployeeId(anyLong())).thenReturn(1L);

        assertThrows(WrongInputException.class, () -> orderAdapter.getOrdersByStatus(userId, page, size, status));
    }

    @Test
    void updateOrderPreparingSuccess() {
        Order order = DomainTestData.getValidOrder(1L);
        OrderEntity orderEntity = PersistenceTestData.getValidOrderEntity(1L);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));
        when(employeePersistencePort.getRestaurantIdByEmployeeId(anyLong())).thenReturn(1L);

        orderAdapter.updateOrderPreparing(order);

        assertAll(
            () -> verify(orderRepository, times(1)).findById(anyLong()),
            () -> verify(orderRepository, times(1)).save(any(OrderEntity.class)),
            () -> verify(employeePersistencePort, times(1)).getRestaurantIdByEmployeeId(anyLong())
        );
    }

    @Test
    void updateOrderPreparingFailOnOrderEmployeeMismatch() {
        Order order = DomainTestData.getValidOrder(1L);
        OrderEntity orderEntity = PersistenceTestData.getValidOrderEntity(1L);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));
        when(employeePersistencePort.getRestaurantIdByEmployeeId(anyLong())).thenReturn(2L);

        assertThrows(RegistryMismatchException.class, () -> orderAdapter.updateOrderPreparing(order));

        assertAll(
                () -> verify(orderRepository, times(1)).findById(anyLong()),
                () -> verify(orderRepository, times(0)).save(any(OrderEntity.class)),
                () -> verify(employeePersistencePort, times(1)).getRestaurantIdByEmployeeId(anyLong())
        );
    }

    @Test
    void updateOrderPreparingFailOnStatusNotPending() {
        Order order = DomainTestData.getValidOrder(1L);
        OrderEntity orderEntity = PersistenceTestData.getValidOrderEntity(1L);
        orderEntity.setStatus("DELIVERED");

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));

        assertThrows(WrongConditionsException.class, () -> orderAdapter.updateOrderPreparing(order));

        assertAll(
                () -> verify(orderRepository, times(1)).findById(anyLong()),
                () -> verify(orderRepository, times(0)).save(any(OrderEntity.class)),
                () -> verify(employeePersistencePort, times(0)).getRestaurantIdByEmployeeId(anyLong())
        );
    }

    @Test
    void updateOrderPreparingFailOnOrderNotFound() {
        Order order = DomainTestData.getValidOrder(1L);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RegistryNotFoundException.class, () -> orderAdapter.updateOrderPreparing(order));

        assertAll(
                () -> verify(orderRepository, times(1)).findById(anyLong()),
                () -> verify(orderRepository, times(0)).save(any(OrderEntity.class)),
                () -> verify(employeePersistencePort, times(0)).getRestaurantIdByEmployeeId(anyLong())
        );
    }

    @Test
    void updateOrderReadySuccess() {
        Order order = DomainTestData.getValidOrder(1L);
        OrderEntity orderEntity = PersistenceTestData.getValidOrderEntity(1L);
        orderEntity.setStatus(DomainConstants.OrderStatus.PREPARING.name());
        Long restaurantId = 1L;

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));
        when(employeePersistencePort.getRestaurantIdByEmployeeId(anyLong())).thenReturn(restaurantId);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);
        when(orderEntityMapper.orderEntityToOrder(any(OrderEntity.class))).thenReturn(order);

        Order updatedOrder = orderAdapter.updateOrderReady(order);

        assertAll(
            () -> assertEquals(order, updatedOrder),
            () -> verify(orderRepository, times(1)).findById(anyLong()),
            () -> verify(orderRepository, times(1)).save(any(OrderEntity.class)),
            () -> verify(employeePersistencePort, times(1)).getRestaurantIdByEmployeeId(anyLong()),
            () -> verify(orderEntityMapper, times(1)).orderEntityToOrder(any(OrderEntity.class))
        );
    }

    @Test
    void updateOrderReadyFailBecauseOrderEmployeeMismatch() {
        Order order = DomainTestData.getValidOrder(1L);
        OrderEntity orderEntity = PersistenceTestData.getValidOrderEntity(1L);
        orderEntity.setStatus(DomainConstants.OrderStatus.PREPARING.name());
        Long restaurantId = 2L;

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));
        when(employeePersistencePort.getRestaurantIdByEmployeeId(anyLong())).thenReturn(restaurantId);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);
        when(orderEntityMapper.orderEntityToOrder(any(OrderEntity.class))).thenReturn(order);

        assertThrows(RegistryMismatchException.class, () -> orderAdapter.updateOrderReady(order));

        assertAll(
            () -> verify(orderRepository, times(1)).findById(anyLong()),
            () -> verify(orderRepository, times(0)).save(any(OrderEntity.class)),
            () -> verify(employeePersistencePort, times(1)).getRestaurantIdByEmployeeId(anyLong()),
            () -> verify(orderEntityMapper, times(0)).orderEntityToOrder(any(OrderEntity.class))
        );
    }

    @Test
    void updateOrderReadyFailBecauseStatusNotPreparing() {
        Order order = DomainTestData.getValidOrder(1L);
        OrderEntity orderEntity = PersistenceTestData.getValidOrderEntity(1L);
        orderEntity.setStatus(DomainConstants.OrderStatus.DELIVERED.name());

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));

        assertThrows(WrongConditionsException.class, () -> orderAdapter.updateOrderReady(order));

        assertAll(
            () -> verify(orderRepository, times(1)).findById(anyLong()),
            () -> verify(orderRepository, times(0)).save(any(OrderEntity.class)),
            () -> verify(employeePersistencePort, times(0)).getRestaurantIdByEmployeeId(anyLong()),
            () -> verify(orderEntityMapper, times(0)).orderEntityToOrder(any(OrderEntity.class))
        );
    }

    @Test
    void updateOrderReadyFailBecauseOrderNotFound() {
        Order order = DomainTestData.getValidOrder(1L);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RegistryNotFoundException.class, () -> orderAdapter.updateOrderReady(order));

        assertAll(
            () -> verify(orderRepository, times(1)).findById(anyLong()),
            () -> verify(orderRepository, times(0)).save(any(OrderEntity.class)),
            () -> verify(employeePersistencePort, times(0)).getRestaurantIdByEmployeeId(anyLong()),
            () -> verify(orderEntityMapper, times(0)).orderEntityToOrder(any(OrderEntity.class))
        );
    }
}