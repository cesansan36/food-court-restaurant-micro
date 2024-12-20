package plazadecomidas.restaurants.adapters.driving.http.rest.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.AddOrderRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.UpdateOrderRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.UpdateOrderToDeliveredRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.response.OrderCancelledResponse;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.response.OrderResponse;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IOrderCancelledResponseMapper;
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
    private final IOrderCancelledResponseMapper orderCancelledResponseMapper;
    private final ITokenUtils tokenUtils;

    @PostMapping("register")
    @PreAuthorize("hasRole('CLIENT')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> registerOrder(@RequestHeader("Authorization") String token, @RequestBody AddOrderRequest addOrderRequest) {
        Long userId = getUserId(token);

        orderServicePort.saveOrder(
                orderRequestMapper.addOrderRequestToOrder(
                        addOrderRequest, userId, ControllerConstants.OrderStatus.PENDING.toString()), token);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("cancel")
    @PreAuthorize("hasRole('CLIENT')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> cancelOrder(@RequestHeader("Authorization") String token, @RequestBody UpdateOrderRequest request) {
        Long userId = getUserId(token);

        OrderCancelledResponse result = orderCancelledResponseMapper.toCancelledResponse(
                orderServicePort.updateOrderCancelled(
                orderRequestMapper.updateOrderCancelledRequestToOrder(
                        request, userId, ControllerConstants.OrderStatus.CANCELLED.name()), token));

        if (result.success()) {
            return ResponseEntity.ok(result.message());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.message());
        }
    }

    @GetMapping("list")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<OrderResponse>> listOrders(
                                @RequestHeader("Authorization") String token,
                                @RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "5") Integer size,
                                @RequestParam(defaultValue = "PENDING") String status
    ) {
        if (page < 0) { page = 0; }
        if (size < 1) { size = 5; }

        Long userId = getUserId(token);

        List<OrderResponse> orders = orderResponseMapper.ordersToOrderResponses(
                                        orderServicePort.getOrdersByStatus(userId, page, size, status));
        return ResponseEntity.ok(orders);
    }

    @PutMapping("assign_chef")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> assignChef(@RequestHeader("Authorization") String token, @RequestBody UpdateOrderRequest request) {
        Long userId = getUserId(token);

        orderServicePort.updateOrderPreparing(
                orderRequestMapper.updateOrderRequestToOrder(
                        request, userId, ControllerConstants.OrderStatus.PREPARING.toString()), token);

        return ResponseEntity.ok().build();
    }

    @PutMapping("set_ready")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> setReady(@RequestHeader("Authorization") String token, @RequestBody UpdateOrderRequest request) {
        Long userId = getUserId(token);

        orderServicePort.updateOrderReady(
                orderRequestMapper.updateOrderRequestToOrder(
                        request, userId, ControllerConstants.OrderStatus.READY.name()), token);

        return ResponseEntity.ok().build();
    }

    @PutMapping("set_delivered")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> setDelivered(@RequestHeader("Authorization") String token, @RequestBody UpdateOrderToDeliveredRequest request) {

        orderServicePort.updateOrderDelivered(
                orderRequestMapper.updateOrderToDeliveredRequestToOrder(
                        request, ControllerConstants.OrderStatus.DELIVERED.name()), token);

        return ResponseEntity.ok().build();
    }

    private Long getUserId(String token) {
        String jwt = token.substring(7);
        return tokenUtils.getSpecificClaim(tokenUtils.validateToken(jwt), ControllerConstants.USER_CLAIM).asLong();
    }
}
