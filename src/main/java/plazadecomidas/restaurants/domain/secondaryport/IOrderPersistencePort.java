package plazadecomidas.restaurants.domain.secondaryport;

import plazadecomidas.restaurants.domain.model.Order;

import java.util.List;

public interface IOrderPersistencePort {
    int getAmountOfUnfinishedOrders(Long clientId);

    Order saveOrder(Order order);

    List<Order> getOrdersByStatus(Long userId, Integer page, Integer size, String status);

    Order updateOrderPreparing(Order order);

    Order updateOrderReady(Order order);

    void updateOrderDelivered(Order order);

    boolean tryCancelOrder(Order order);
}
