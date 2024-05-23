package plazadecomidas.restaurants.domain.primaryport;

import plazadecomidas.restaurants.domain.model.Order;

public interface IOrderServicePort {
    void saveOrder(Order order);
}
