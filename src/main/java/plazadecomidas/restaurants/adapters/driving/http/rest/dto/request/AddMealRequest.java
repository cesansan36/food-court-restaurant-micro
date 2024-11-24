package plazadecomidas.restaurants.adapters.driving.http.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AddMealRequest {

    private String name;
    private String description;
    private Long price;
    private String imageUrl;
    private Long restaurantId;
    private Long categoryId;
}
