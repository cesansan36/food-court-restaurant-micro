package plazadecomidas.restaurants.adapters.driving.http.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.AddOrderRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.response.OrderResponse;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IOrderRequestMapper;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IOrderResponseMapper;
import plazadecomidas.restaurants.adapters.driving.http.rest.util.ControllerConstants;
import plazadecomidas.restaurants.domain.primaryport.IOrderServicePort;
import plazadecomidas.restaurants.util.ITokenUtils;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderControllerAdapter {

    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;
    private final IOrderResponseMapper orderResponseMapper;
    private final ITokenUtils tokenUtils;

    @PostMapping("register")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> registerOrder(@RequestHeader("Authorization") String token, @RequestBody AddOrderRequest addOrderRequest) {
        Long userId = getUserId(token);

        orderServicePort.saveOrder(
                orderRequestMapper.addOrderRequestToOrder(
                        addOrderRequest, userId, ControllerConstants.OrderStatus.PENDING.toString()));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("list")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<OrderResponse>> listOrders(
                                @RequestHeader("Authorization") String token,
                                @RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "5") Integer size,
                                @RequestParam(defaultValue = "PENDING") String status
    ) {
        if (page < 0) { page = 0; }
        if (size < 1) { size = 5; }

        // TODO: Aclarar como se relaciona un empleado con el restaurante
        Long userId = getUserId(token);

        List<OrderResponse> orders = orderResponseMapper.ordersToOrderResponses(
                                        orderServicePort.getOrdersByStatus(userId, page, size, status));
        return ResponseEntity.ok(orders);
    }

    private Long getUserId(String token) {
        String jwt = token.substring(7);
        return tokenUtils.getSpecificClaim(tokenUtils.validateToken(jwt), ControllerConstants.USER_CLAIM).asLong();
    }
}
