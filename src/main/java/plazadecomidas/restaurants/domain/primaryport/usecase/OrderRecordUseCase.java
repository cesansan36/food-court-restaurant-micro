package plazadecomidas.restaurants.domain.primaryport.usecase;

import plazadecomidas.restaurants.domain.model.Order;
import plazadecomidas.restaurants.domain.model.OrderRecord;
import plazadecomidas.restaurants.domain.primaryport.IOrderRecordPrimaryPort;
import plazadecomidas.restaurants.domain.secondaryport.ITracingConnectionPort;
import plazadecomidas.restaurants.domain.secondaryport.IUserConnectionPort;
import plazadecomidas.restaurants.domain.util.DomainConstants;

import java.time.LocalDateTime;
import java.util.List;

public class OrderRecordUseCase implements IOrderRecordPrimaryPort {

    private final ITracingConnectionPort tracingConnectionPort;
    private final IUserConnectionPort userConnectionPort;

    public OrderRecordUseCase(ITracingConnectionPort tracingConnectionPort, IUserConnectionPort userConnectionPort) {
        this.tracingConnectionPort = tracingConnectionPort;
        this.userConnectionPort = userConnectionPort;
    }

    @Override
    public List<OrderRecord> listRecords(String token,Long clientId) {
        return tracingConnectionPort.list(token, clientId);
    }

    @Override
    public void createOrderRecord(Order order, String token) {
        String userEmail = userConnectionPort.getUserEmail(token);

        OrderRecord orderRecord = new OrderRecord(
                null,
                order.getId(),
                order.getIdClient(),
                userEmail,
                LocalDateTime.now(),
                DomainConstants.OrderStatus.NOT_CREATED.name(),
                DomainConstants.OrderStatus.PENDING.name(),
                null,
                null
        );

        tracingConnectionPort.create(token, orderRecord);
    }

    @Override
    public void assignChefToOrder(Order order, String token) {
        String userEmail = userConnectionPort.getUserEmail(token);
        OrderRecord recordOfPreviousState = tracingConnectionPort.findRecordByOrderIdAndStatus(order.getId(), DomainConstants.OrderStatus.PENDING.name(), token);

        OrderRecord updatedRecord = new OrderRecord(
                recordOfPreviousState.getId(),
                recordOfPreviousState.getIdOrder(),
                recordOfPreviousState.getIdClient(),
                recordOfPreviousState.getClientEmail(),
                LocalDateTime.now(),
                DomainConstants.OrderStatus.PENDING.name(),
                DomainConstants.OrderStatus.PREPARING.name(),
                order.getIdChef(),
                userEmail
        );

        tracingConnectionPort.create(token, updatedRecord);
    }

    @Override
    public void updateOrderToReady(Order updatedOrder, String token) {

        OrderRecord recordOfPreviousState = tracingConnectionPort.findRecordByOrderIdAndStatus(
                updatedOrder.getId(), DomainConstants.OrderStatus.PREPARING.name(), token);

        OrderRecord updatedRecord = new OrderRecord(
                recordOfPreviousState.getId(),
                recordOfPreviousState.getIdOrder(),
                recordOfPreviousState.getIdClient(),
                recordOfPreviousState.getClientEmail(),
                LocalDateTime.now(),
                DomainConstants.OrderStatus.PREPARING.name(),
                DomainConstants.OrderStatus.READY.name(),
                recordOfPreviousState.getIdEmployee(),
                recordOfPreviousState.getEmployeeEmail()
        );

        tracingConnectionPort.create(token, updatedRecord);
    }

    @Override
    public void updateOrderToDelivered(Order order, String token) {

        OrderRecord recordOfPreviousState = tracingConnectionPort.findRecordByOrderIdAndStatus(
                order.getId(), DomainConstants.OrderStatus.READY.name(), token);

        OrderRecord updatedRecord = new OrderRecord(
                recordOfPreviousState.getId(),
                recordOfPreviousState.getIdOrder(),
                recordOfPreviousState.getIdClient(),
                recordOfPreviousState.getClientEmail(),
                LocalDateTime.now(),
                DomainConstants.OrderStatus.READY.name(),
                DomainConstants.OrderStatus.DELIVERED.name(),
                recordOfPreviousState.getIdEmployee(),
                recordOfPreviousState.getEmployeeEmail()
        );

        tracingConnectionPort.create(token, updatedRecord);
    }

    @Override
    public void updateOrderToCancelled(Order order, String token) {

        OrderRecord recordOfPreviousState = tracingConnectionPort.findRecordByOrderIdAndStatus(
                order.getId(), DomainConstants.OrderStatus.PENDING.name(), token);

        OrderRecord updatedRecord = new OrderRecord(
                recordOfPreviousState.getId(),
                recordOfPreviousState.getIdOrder(),
                recordOfPreviousState.getIdClient(),
                recordOfPreviousState.getClientEmail(),
                LocalDateTime.now(),
                DomainConstants.OrderStatus.PENDING.name(),
                DomainConstants.OrderStatus.CANCELLED.name(),
                recordOfPreviousState.getIdEmployee(),
                recordOfPreviousState.getEmployeeEmail()
        );

        tracingConnectionPort.create(token, updatedRecord);
    }
}
