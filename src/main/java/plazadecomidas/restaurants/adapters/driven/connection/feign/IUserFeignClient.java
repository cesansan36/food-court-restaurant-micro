package plazadecomidas.restaurants.adapters.driven.connection.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user", url = "http://localhost:8090/users")
public interface IUserFeignClient {

    @GetMapping("user/verify-role")
    ResponseEntity<Boolean> verifyRole(@RequestParam Long id, @RequestParam String role);

    @GetMapping("/get-number")
    ResponseEntity<String> getNumber(@RequestHeader("Authorization") String token, @RequestParam Long id);

    @GetMapping("/get-email")
    ResponseEntity<String> getEmail(@RequestHeader("Authorization") String token);
}
