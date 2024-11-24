package plazadecomidas.restaurants.adapters.driving.http.rest.dto.response;

import plazadecomidas.restaurants.domain.model.Category;

public record MealResponse(
        Long id,
        String name,
        String description,
        Long price,
        String imageUrl,
        Category category
) {
}
