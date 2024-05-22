package plazadecomidas.restaurants.adapters.driving.http.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import plazadecomidas.restaurants.TestData.DomainTestData;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.AddRestaurantRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.response.RestaurantResponse;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IRestaurantRequestMapper;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IRestaurantResponseMapper;
import plazadecomidas.restaurants.domain.model.Restaurant;
import plazadecomidas.restaurants.domain.primaryport.IRestaurantServicePort;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerAdapterTest {

    private RestaurantControllerAdapter restaurantControllerAdapter;

    private IRestaurantServicePort restaurantServicePort;
    private IRestaurantRequestMapper restaurantRequestMapper;
    private IRestaurantResponseMapper restaurantResponseMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        restaurantServicePort = mock(IRestaurantServicePort.class);
        restaurantRequestMapper = mock(IRestaurantRequestMapper.class);
        restaurantResponseMapper = mock(IRestaurantResponseMapper.class);
        restaurantControllerAdapter = new RestaurantControllerAdapter(restaurantServicePort, restaurantRequestMapper, restaurantResponseMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(restaurantControllerAdapter).build();
    }

    @Test
    void addRestaurant() throws Exception {

        Object inputObject = new Object() {
            public final String name = "Corrientazo";
            public final String address = "por ahí";
            public final String ownerId = "1";
            public final String phoneNumber = "123456789";
            public final String logoUrl = "https://picsum.photos/200";
            public final String nit = "123";
        };
        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(inputObject);

        when(restaurantRequestMapper.addRestaurantRequestToRestaurant(any(AddRestaurantRequest.class))).thenReturn(DomainTestData.getValidRestaurant(1L, 1L));

        MockHttpServletRequestBuilder request = post("/restaurants/register/restaurant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated());

        verify(restaurantRequestMapper, times(1)).addRestaurantRequestToRestaurant(any(AddRestaurantRequest.class));
        verify(restaurantServicePort, times(1)).saveRestaurant(any());
    }

    @Test
    void listRestaurants() throws Exception {

        RestaurantResponse response = new RestaurantResponse("Corrientazo", "por ahí", "123456789", "https://picsum.photos/200");
        List<Restaurant> restaurants = List.of(DomainTestData.getValidRestaurant(1L, 1L));

        when(restaurantServicePort.getAllRestaurants()).thenReturn(restaurants);
        when(restaurantResponseMapper.restaurantsToRestaurantResponses(anyList())).thenReturn(List.of(response));

        MockHttpServletRequestBuilder request = get("/restaurants/list");

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Corrientazo"))
                .andExpect(jsonPath("$[0].address").value("por ahí"))
                .andExpect(jsonPath("$[0].phoneNumber").value("123456789"))
                .andExpect(jsonPath("$[0].logoUrl").value("https://picsum.photos/200"));

        verify(restaurantServicePort, times(1)).getAllRestaurants();
        verify(restaurantResponseMapper, times(1)).restaurantsToRestaurantResponses(anyList());
    }
}