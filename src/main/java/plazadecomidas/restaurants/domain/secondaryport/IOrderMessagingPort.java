package plazadecomidas.restaurants.domain.secondaryport;

import plazadecomidas.restaurants.domain.model.Order;

public interface IOrderMessagingPort {
    void sendOrderReadyMessage(String token, Order order, String phoneNumber);
}
