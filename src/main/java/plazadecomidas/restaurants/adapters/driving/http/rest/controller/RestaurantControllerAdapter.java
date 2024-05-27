package plazadecomidas.restaurants.adapters.driving.http.rest.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.AddRestaurantRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.response.RestaurantResponse;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IRestaurantRequestMapper;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IRestaurantResponseMapper;
import plazadecomidas.restaurants.domain.primaryport.IRestaurantServicePort;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@AllArgsConstructor
public class RestaurantControllerAdapter {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;
    private final IRestaurantResponseMapper restaurantResponseMapper;

    @PostMapping("register/restaurant")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> addRestaurant(@RequestBody AddRestaurantRequest addRestaurantRequest) {

        restaurantServicePort.saveRestaurant(
                restaurantRequestMapper.addRestaurantRequestToRestaurant(addRestaurantRequest));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("list")
    @PreAuthorize("hasRole('CLIENT')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<RestaurantResponse>> listRestaurants() {

        List<RestaurantResponse> restaurants = restaurantResponseMapper.restaurantsToRestaurantResponses(
                restaurantServicePort.getAllRestaurants());

        return ResponseEntity.ok(restaurants);
    }
}
