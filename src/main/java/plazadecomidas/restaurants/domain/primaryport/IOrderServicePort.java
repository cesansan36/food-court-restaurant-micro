package plazadecomidas.restaurants.domain.primaryport;

import plazadecomidas.restaurants.domain.model.OperationResult;
import plazadecomidas.restaurants.domain.model.Order;

import java.util.List;

public interface IOrderServicePort {
    void saveOrder(Order order, String token);

    List<Order> getOrdersByStatus(Long userId, Integer page, Integer size, String status);

    void updateOrderPreparing(Order order, String token);

    void updateOrderReady(Order order, String token);

    void updateOrderDelivered(Order order, String token);

    OperationResult updateOrderCancelled(Order order, String token);
}
