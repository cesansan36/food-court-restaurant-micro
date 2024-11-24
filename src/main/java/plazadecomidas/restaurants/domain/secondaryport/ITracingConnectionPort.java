package plazadecomidas.restaurants.domain.secondaryport;

import plazadecomidas.restaurants.domain.model.OrderRecord;

import java.util.List;

public interface ITracingConnectionPort {
    OrderRecord create(String token, OrderRecord request);

    List<OrderRecord> list(String token, Long clientId);

    OrderRecord findRecordByOrderIdAndStatus(Long id, String name, String token);
}
