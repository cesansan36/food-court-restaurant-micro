package plazadecomidas.restaurants.adapters.driving.http.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AddRestaurantRequest {

    private String name;
    private String address;
    private Long ownerId;
    private String phoneNumber;
    private String logoUrl;
    private String nit;
}
