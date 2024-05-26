package plazadecomidas.restaurants.domain.primaryport.usecase;

import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryNotFoundException;
import plazadecomidas.restaurants.domain.exception.ClientHasUnfinishedOrdersException;
import plazadecomidas.restaurants.domain.model.Order;
import plazadecomidas.restaurants.domain.primaryport.IOrderServicePort;
import plazadecomidas.restaurants.domain.secondaryport.IOrderMessagingPort;
import plazadecomidas.restaurants.domain.secondaryport.IOrderPersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IUserConnectionPort;
import plazadecomidas.restaurants.domain.util.DomainConstants;

import java.util.List;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IUserConnectionPort userConnectionPort;
    private final IOrderMessagingPort orderMessagingPort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IUserConnectionPort userConnectionPort, IOrderMessagingPort orderMessagingPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.userConnectionPort = userConnectionPort;
        this.orderMessagingPort = orderMessagingPort;
    }

    @Override
    public void saveOrder(Order order) {

        int unfinishedOrders = orderPersistencePort.getAmountOfUnfinishedOrders(order.getIdClient());
        if (unfinishedOrders > 0) {
            throw new ClientHasUnfinishedOrdersException(DomainConstants.CLIENT_HAS_UNFINISHED_ORDERS_MESSAGE);
        }

        orderPersistencePort.saveOrder(order);
    }

    @Override
    public List<Order> getOrdersByStatus(Long userId, Integer page, Integer size, String status) {
        return orderPersistencePort.getOrdersByStatus(userId, page, size, status);
    }

    @Override
    public void updateOrderPreparing(Order order) {
        orderPersistencePort.updateOrderPreparing(order);
    }

    @Override
    public void updateOrderReady(Order order, String token) {

        Order updatedOrder = orderPersistencePort.updateOrderReady(order);
        String phoneNumber;

        try {
            phoneNumber = userConnectionPort.getUserPhoneNumber(updatedOrder.getIdClient(), token);
        } catch (Exception e) {
            throw new RegistryNotFoundException(DomainConstants.USER_ID_NOT_FOUND_MESSAGE);
        }

        orderMessagingPort.sendOrderReadyMessage(token, updatedOrder, phoneNumber);
    }
}
