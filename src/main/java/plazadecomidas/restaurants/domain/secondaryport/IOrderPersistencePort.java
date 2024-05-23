package plazadecomidas.restaurants.domain.secondaryport;

import plazadecomidas.restaurants.domain.model.Order;

public interface IOrderPersistencePort {
    int getAmountOfUnfinishedOrders(Long clientId);

    void saveOrder(Order order);
}
