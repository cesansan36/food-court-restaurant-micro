package plazadecomidas.restaurants.adapters.driven.connection.adapter;

import lombok.RequiredArgsConstructor;
import plazadecomidas.restaurants.adapters.driven.connection.dto.request.SendMessageRequest;
import plazadecomidas.restaurants.adapters.driven.connection.feign.IMessagingFeignClient;
import plazadecomidas.restaurants.adapters.driven.connection.mapper.ISendMessageRequestMapper;
import plazadecomidas.restaurants.domain.model.Order;
import plazadecomidas.restaurants.domain.secondaryport.IOrderMessagingPort;

@RequiredArgsConstructor
public class MessagingAdapter implements IOrderMessagingPort {

    private final ISendMessageRequestMapper sendMessageRequestMapper;
    private final IMessagingFeignClient messagingFeignClient;

    @Override
    public void sendOrderReadyMessage(String token, Order order, String phoneNumber) {
        SendMessageRequest request = sendMessageRequestMapper.toSendMessageRequest(order, phoneNumber);

        messagingFeignClient.sendMessage(token, request);
    }
}
