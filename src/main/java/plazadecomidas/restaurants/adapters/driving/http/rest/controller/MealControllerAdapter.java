package plazadecomidas.restaurants.adapters.driving.http.rest.controller;

import lombok.AllArgsConstructor;
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
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.AddMealRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.UpdateMealAvailabilityRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.UpdateMealRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.response.MealResponse;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IMealRequestMapper;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IMealResponseMapper;
import plazadecomidas.restaurants.adapters.driving.http.rest.util.ControllerConstants;
import plazadecomidas.restaurants.domain.model.Meal;
import plazadecomidas.restaurants.domain.primaryport.IMealServicePort;
import plazadecomidas.restaurants.util.ITokenUtils;

import java.util.List;

@RestController
@RequestMapping("/meals")
@AllArgsConstructor
public class MealControllerAdapter {

    private final IMealServicePort mealServicePort;
    private final IMealRequestMapper mealRequestMapper;
    private final IMealResponseMapper mealResponseMapper;
    private final ITokenUtils tokenUtils;

    @PostMapping("register")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> addMeal(@RequestHeader("Authorization") String token, @RequestBody AddMealRequest addMealRequest) {
        Long userId = getUserId(token);
        Meal meal = mealRequestMapper.addMealRequestToMeal(addMealRequest);

        mealServicePort.saveMeal(meal, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("update")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> updateMeal(@RequestHeader("Authorization") String token, @RequestBody UpdateMealRequest updateMealRequest) {
        Long userId = getUserId(token);
        Meal meal = mealRequestMapper.updateMealRequestToMeal(updateMealRequest);

        mealServicePort.updateMeal(meal, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("set-availability")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> setAvailability(@RequestHeader("Authorization") String token,
                                                @RequestBody UpdateMealAvailabilityRequest updateMealRequest) {
        Long userId = getUserId(token);
        Meal meal = mealRequestMapper.updateMealAvailabilityRequestToMeal(updateMealRequest);

        mealServicePort.updateMealAvailability(meal, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("list")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<MealResponse>> listMeals(@RequestParam Long restaurantId) {

        List<MealResponse> results = mealResponseMapper.mealsToMealResponses(mealServicePort.getMealsOfRestaurant(restaurantId));

        return ResponseEntity.ok(results);
    }

    private Long getUserId(String token) {
        String jwt = token.substring(7);
        return tokenUtils.getSpecificClaim(tokenUtils.validateToken(jwt), ControllerConstants.USER_CLAIM).asLong();
    }
}
