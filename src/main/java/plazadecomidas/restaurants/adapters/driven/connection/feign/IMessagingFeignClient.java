package plazadecomidas.restaurants.adapters.driven.connection.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import plazadecomidas.restaurants.adapters.driven.connection.dto.request.SendMessageRequest;

@FeignClient(name = "message", url = "http://localhost:8180/messages")
public interface IMessagingFeignClient {

    @PostMapping("send")
    ResponseEntity<String> sendMessage(@RequestHeader("Authorization") String token, @RequestBody SendMessageRequest sendMessageRequest);
}
