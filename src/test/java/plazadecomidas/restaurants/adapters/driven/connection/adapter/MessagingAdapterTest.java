package plazadecomidas.restaurants.adapters.driven.connection.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plazadecomidas.restaurants.adapters.driven.connection.dto.SendMessageRequest;
import plazadecomidas.restaurants.adapters.driven.connection.feign.IMessagingFeignClient;
import plazadecomidas.restaurants.adapters.driven.connection.mapper.ISendMessageRequestMapper;
import plazadecomidas.restaurants.domain.model.Order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MessagingAdapterTest {

    private MessagingAdapter messagingAdapter;

    private ISendMessageRequestMapper sendMessageRequestMapper;
    private IMessagingFeignClient messagingFeignClient;

    @BeforeEach
    void setUp() {

        sendMessageRequestMapper = mock(ISendMessageRequestMapper.class);
        messagingFeignClient = mock(IMessagingFeignClient.class);
        messagingAdapter = new MessagingAdapter(sendMessageRequestMapper, messagingFeignClient);
    }

    @Test
    void sendOrderReadyMessage() {
        String bearerToken = "Bearer 123456789";
        Order order = mock(Order.class);
        String phoneNumber = "123456789";

        SendMessageRequest sendMessageRequest = mock(SendMessageRequest.class);

        when(sendMessageRequestMapper.toSendMessageRequest(any(Order.class), anyString())).thenReturn(sendMessageRequest);

        messagingAdapter.sendOrderReadyMessage(bearerToken, order, phoneNumber);

        verify(messagingFeignClient, times(1)).sendMessage(anyString(), any(SendMessageRequest.class));
        verify(sendMessageRequestMapper, times(1)).toSendMessageRequest(any(Order.class), anyString());
    }
}