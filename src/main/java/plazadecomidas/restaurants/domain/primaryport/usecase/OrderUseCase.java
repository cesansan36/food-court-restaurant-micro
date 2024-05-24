package plazadecomidas.restaurants.domain.primaryport.usecase;

import plazadecomidas.restaurants.domain.exception.ClientHasUnfinishedOrdersException;
import plazadecomidas.restaurants.domain.model.Order;
import plazadecomidas.restaurants.domain.primaryport.IOrderServicePort;
import plazadecomidas.restaurants.domain.secondaryport.IOrderPersistencePort;
import plazadecomidas.restaurants.domain.util.DomainConstants;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
    }

    @Override
    public void saveOrder(Order order) {

        int unfinishedOrders = orderPersistencePort.getAmountOfUnfinishedOrders(order.getIdClient());
        if (unfinishedOrders > 0) {
            throw new ClientHasUnfinishedOrdersException(DomainConstants.CLIENT_HAS_UNFINISHED_ORDERS_MESSAGE);
        }

        orderPersistencePort.saveOrder(order);
    }
}