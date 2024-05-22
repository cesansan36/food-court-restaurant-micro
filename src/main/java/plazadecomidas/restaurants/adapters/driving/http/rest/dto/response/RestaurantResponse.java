package plazadecomidas.restaurants.adapters.driving.http.rest.dto.response;

public record RestaurantResponse(

        Long id,
        String name,
        String address,
        String phoneNumber,
        String logoUrl
) {
}
