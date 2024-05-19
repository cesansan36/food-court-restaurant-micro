package plazadecomidas.restaurants.adapters.driving.http.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.AddRestaurantRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IRestaurantRequestMapper;
import plazadecomidas.restaurants.domain.primaryport.IRestaurantServicePort;

@RestController
@RequestMapping("/restaurants")
@AllArgsConstructor
public class RestaurantControllerAdapter {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;

    @PostMapping("register/restaurant")
    public ResponseEntity<Void> addRestaurant(@RequestBody AddRestaurantRequest addRestaurantRequest) {

        restaurantServicePort.saveRestaurant(restaurantRequestMapper.addRestaurantRequestToRestaurant(addRestaurantRequest));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
