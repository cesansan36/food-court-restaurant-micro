package plazadecomidas.restaurants.domain.primaryport;

import plazadecomidas.restaurants.domain.model.Order;

import java.util.List;

public interface IOrderServicePort {
    void saveOrder(Order order);

    List<Order> getOrdersByStatus(Long userId, Integer page, Integer size, String status);

    void updateOrderPreparing(Order order);
}
