package plazadecomidas.restaurants.domain.primaryport.usecase;

import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryNotFoundException;
import plazadecomidas.restaurants.domain.exception.ClientHasUnfinishedOrdersException;
import plazadecomidas.restaurants.domain.model.OperationResult;
import plazadecomidas.restaurants.domain.model.Order;
import plazadecomidas.restaurants.domain.primaryport.IOrderRecordPrimaryPort;
import plazadecomidas.restaurants.domain.primaryport.IOrderServicePort;
import plazadecomidas.restaurants.domain.secondaryport.IOrderMessagingPort;
import plazadecomidas.restaurants.domain.secondaryport.IOrderPersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IUserConnectionPort;
import plazadecomidas.restaurants.domain.util.DomainConstants;

import java.util.List;
import java.util.Random;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IUserConnectionPort userConnectionPort;
    private final IOrderMessagingPort orderMessagingPort;
    private final IOrderRecordPrimaryPort orderRecordPrimaryPort;
    private final Random random;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IUserConnectionPort userConnectionPort, IOrderMessagingPort orderMessagingPort, IOrderRecordPrimaryPort orderRecordPrimaryPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.userConnectionPort = userConnectionPort;
        this.orderMessagingPort = orderMessagingPort;
        this.orderRecordPrimaryPort = orderRecordPrimaryPort;

        random = new Random();
    }

    @Override
    public void saveOrder(Order order, String token) {

        int unfinishedOrders = orderPersistencePort.getAmountOfUnfinishedOrders(order.getIdClient());
        if (unfinishedOrders > 0) {
            throw new ClientHasUnfinishedOrdersException(DomainConstants.CLIENT_HAS_UNFINISHED_ORDERS_MESSAGE);
        }

        Integer securityPin = random.nextInt(9000) + 1000;

        Order orderWithPin = new Order(
                order.getId(),
                order.getIdClient(),
                order.getDate(),
                order.getStatus(),
                securityPin,
                order.getIdChef(),
                order.getIdRestaurant(),
                order.getMeals()
        );

        Order savedOrder = orderPersistencePort.saveOrder(orderWithPin);

        orderRecordPrimaryPort.createOrderRecord(savedOrder, token);
    }

    @Override
    public List<Order> getOrdersByStatus(Long userId, Integer page, Integer size, String status) {
        return orderPersistencePort.getOrdersByStatus(userId, page, size, status);
    }

    @Override
    public void updateOrderPreparing(Order order, String token) {
        Order updatedOrder = orderPersistencePort.updateOrderPreparing(order);

        orderRecordPrimaryPort.assignChefToOrder(updatedOrder, token);
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

        orderRecordPrimaryPort.updateOrderToReady(updatedOrder, token);
        orderMessagingPort.sendOrderReadyMessage(token, updatedOrder, phoneNumber);
    }

    @Override
    public void updateOrderDelivered(Order order, String token) {
        orderPersistencePort.updateOrderDelivered(order);
        orderRecordPrimaryPort.updateOrderToDelivered(order, token);
    }

    @Override
    public OperationResult updateOrderCancelled(Order order, String token) {
        boolean result = orderPersistencePort.tryCancelOrder(order);

        if (result) {
            orderRecordPrimaryPort.updateOrderToCancelled(order, token);
            return new OperationResult(true, DomainConstants.ORDER_CANCELLED_SUCCESS_MESSAGE);
        } else {
            return new OperationResult(false, DomainConstants.ORDER_CANCELLED_FAILED_MESSAGE);
        }
    }
}
