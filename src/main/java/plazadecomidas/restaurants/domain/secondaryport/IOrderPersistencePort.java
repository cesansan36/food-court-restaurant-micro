package plazadecomidas.restaurants.domain.secondaryport;

import plazadecomidas.restaurants.domain.model.Order;

import java.util.List;

public interface IOrderPersistencePort {
    int getAmountOfUnfinishedOrders(Long clientId);

    void saveOrder(Order order);

    List<Order> getOrdersByStatus(Integer page, Integer size, String status);
}
