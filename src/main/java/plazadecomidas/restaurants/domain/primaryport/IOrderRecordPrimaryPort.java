package plazadecomidas.restaurants.domain.primaryport;

import plazadecomidas.restaurants.domain.model.Order;
import plazadecomidas.restaurants.domain.model.OrderRecord;

import java.util.List;

public interface IOrderRecordPrimaryPort {

    List<OrderRecord> listRecords(String token, Long clientId);

    void createOrderRecord(Order order, String token);

    void assignChefToOrder(Order updatedOrder, String token);

    void updateOrderToReady(Order updatedOrder, String token);

    void updateOrderToDelivered(Order order, String token);

    void updateOrderToCancelled(Order order, String token);
}
