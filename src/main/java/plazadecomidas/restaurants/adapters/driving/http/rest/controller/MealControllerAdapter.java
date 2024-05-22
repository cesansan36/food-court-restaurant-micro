package plazadecomidas.restaurants.adapters.driving.http.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.AddMealRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.UpdateMealRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IMealRequestMapper;
import plazadecomidas.restaurants.domain.model.Meal;
import plazadecomidas.restaurants.domain.primaryport.IMealServicePort;

@RestController
@RequestMapping("/meals")
@AllArgsConstructor
public class MealControllerAdapter {

    private final IMealServicePort mealServicePort;
    private final IMealRequestMapper mealRequestMapper;

    @PostMapping("register")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> addMeal(@RequestBody AddMealRequest addMealRequest) {
        Meal meal = mealRequestMapper.addMealRequestToMeal(addMealRequest);
        mealServicePort.saveMeal(meal);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("update")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> updateMeal(@RequestBody UpdateMealRequest updateMealRequest) {
        Meal meal = mealRequestMapper.updateMealRequestToMeal(updateMealRequest);

        mealServicePort.updateMeal(meal);
        return ResponseEntity.ok().build();
    }
}
