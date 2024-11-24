package plazadecomidas.restaurants.adapters.driven.connection.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import plazadecomidas.restaurants.adapters.driven.connection.dto.request.AddRecordRequestFeign;
import plazadecomidas.restaurants.adapters.driven.connection.dto.response.RecordResponseFeign;

import java.util.List;

@FeignClient(name = "tracing", url = "http://localhost:8190/record")
public interface ITracingFeignClient {

    @PostMapping("/create")
    ResponseEntity<RecordResponseFeign> create(@RequestHeader("Authorization") String token, @RequestBody AddRecordRequestFeign request);

    @GetMapping("/list")
    ResponseEntity<List<RecordResponseFeign>> list(@RequestHeader("Authorization") String token, @RequestParam Long clientId);

    @GetMapping("get-order")
    ResponseEntity<RecordResponseFeign> findRecordByOrderIdAndStatus(@RequestHeader("Authorization") String token, @RequestParam Long orderId, @RequestParam String currentState);
}
