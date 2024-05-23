package plazadecomidas.restaurants.domain.secondaryport;

import plazadecomidas.restaurants.domain.model.Order;

public interface IOrderPersistencePort {
    void saveOrder(Order order);
}
