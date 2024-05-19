package plazadecomidas.restaurants.adapters.driven.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user", url = "http://localhost:8090/users")
public interface IUserFeignClient {

    @GetMapping("user/verify-role")
    public ResponseEntity<Boolean> verifyRole(@RequestParam Long id, @RequestParam String role);
}
