package plazadecomidas.restaurants.adapters.driving.http.rest.controller;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import plazadecomidas.restaurants.TestData.ControllerTestData;
import plazadecomidas.restaurants.TestData.DomainTestData;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.AddMealRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.UpdateMealAvailabilityRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.UpdateMealRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.response.MealResponse;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IMealRequestMapper;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IMealResponseMapper;
import plazadecomidas.restaurants.domain.model.Category;
import plazadecomidas.restaurants.domain.model.Meal;
import plazadecomidas.restaurants.domain.primaryport.IMealServicePort;
import plazadecomidas.restaurants.util.ITokenUtils;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MealControllerAdapterTest {

    private MealControllerAdapter mealControllerAdapter;

    private IMealServicePort mealServicePort;
    private IMealRequestMapper mealRequestMapper;
    private IMealResponseMapper mealResponseMapper;
    private ITokenUtils tokenUtils;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mealServicePort = mock(IMealServicePort.class);
        mealRequestMapper = mock(IMealRequestMapper.class);
        mealResponseMapper = mock(IMealResponseMapper.class);
        tokenUtils = mock(ITokenUtils.class);

        mealControllerAdapter = new MealControllerAdapter(mealServicePort, mealRequestMapper, mealResponseMapper, tokenUtils);

        mockMvc = MockMvcBuilders.standaloneSetup(mealControllerAdapter).build();
    }

    @Test
    void addMeal() throws Exception {

        Object inputObject = new Object() {
            public final String name = "Chocolate";
            public final String description = "Delicious";
            public final Long price = 123456L;
            public final String imageUrl = "https://picsum.photos/200";
            public final Long restaurantId = 1L;
            public final Long categoryId = 1L;
        };

        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(inputObject);

        Meal meal = DomainTestData.getValidMeal(1L);
        Claim claim = ControllerTestData.getIdClaim(1L);
        String bearerToken = "Bearer 123456789";

        when(mealRequestMapper.addMealRequestToMeal(any(AddMealRequest.class))).thenReturn(meal);
        when(tokenUtils.validateToken(anyString())).thenReturn(mock(DecodedJWT.class));
        when(tokenUtils.getSpecificClaim(any(DecodedJWT.class), any(String.class))).thenReturn(claim);

        MockHttpServletRequestBuilder request = post("/meals/register")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .content(inputJson);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated());

        verify(mealRequestMapper, times(1)).addMealRequestToMeal(any(AddMealRequest.class));
        verify(mealServicePort, times(1)).saveMeal(any(Meal.class), anyLong());
        verify(tokenUtils, times(1)).validateToken(anyString());
        verify(tokenUtils, times(1)).getSpecificClaim(any(DecodedJWT.class), any(String.class));
    }

    @Test
    void updateMeal() throws Exception {
        Object inputObject = new Object() {
            public final String name = "Chocolate";
            public final String description = "Delicious";
            public final Long price = 123456L;
        };

        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(inputObject);

        Meal meal = DomainTestData.getValidMeal(1L);
        Claim claim = ControllerTestData.getIdClaim(1L);
        String bearerToken = "Bearer 123456789";

        when(mealRequestMapper.updateMealRequestToMeal(any(UpdateMealRequest.class))).thenReturn(meal);
        when(tokenUtils.validateToken(anyString())).thenReturn(mock(DecodedJWT.class));
        when(tokenUtils.getSpecificClaim(any(DecodedJWT.class), any(String.class))).thenReturn(claim);

        MockHttpServletRequestBuilder request = put("/meals/update")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .content(inputJson);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        verify(mealRequestMapper, times(1)).updateMealRequestToMeal(any(UpdateMealRequest.class));
        verify(mealServicePort, times(1)).updateMeal(any(Meal.class), anyLong());
        verify(tokenUtils, times(1)).validateToken(anyString());
        verify(tokenUtils, times(1)).getSpecificClaim(any(DecodedJWT.class), any(String.class));
    }

    @Test
    void updateMealAvailability() throws Exception {
        Object inputObject = new Object() {
            public final String name = "Chocolate";
            public final Boolean availability = true;
            public final Long restaurantId = 1L;
        };

        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(inputObject);

        Meal meal = DomainTestData.getValidMeal(1L);
        Claim claim = ControllerTestData.getIdClaim(1L);
        String bearerToken = "Bearer 123456789";

        when(mealRequestMapper.updateMealAvailabilityRequestToMeal(any(UpdateMealAvailabilityRequest.class))).thenReturn(meal);
        when(tokenUtils.validateToken(anyString())).thenReturn(mock(DecodedJWT.class));
        when(tokenUtils.getSpecificClaim(any(DecodedJWT.class), any(String.class))).thenReturn(claim);

        MockHttpServletRequestBuilder request = put("/meals/set-availability")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .content(inputJson);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        verify(mealRequestMapper, times(1)).updateMealAvailabilityRequestToMeal(any(UpdateMealAvailabilityRequest.class));
        verify(mealServicePort, times(1)).updateMealAvailability(any(Meal.class), anyLong());
        verify(tokenUtils, times(1)).validateToken(anyString());
        verify(tokenUtils, times(1)).getSpecificClaim(any(DecodedJWT.class), any(String.class));
    }

    @Test
    void getAllActiveMealsFromRestaurant() throws Exception {
        Long restaurantId = 1L;
        Integer page = 0;
        Integer size = 5;
        Long idCategory = 1L;
        String bearerToken = "Bearer 123456789";

        Meal meal = DomainTestData.getValidMeal(1L);
        MealResponse response = new MealResponse(1L, "Chocolate", "Delicious", 123456L, "https://picsum.photos/200", new Category(1L, "Desayuno", "Delicious"));

        when(mealServicePort.getMealsOfRestaurant(anyLong(), anyInt(), anyInt(), anyLong())).thenReturn(List.of(meal));
        when(mealResponseMapper.mealsToMealResponses(anyList())).thenReturn(List.of(response));


        MockHttpServletRequestBuilder request = get("/meals/list")
                .param("restaurantId", String.valueOf(restaurantId))
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("idCategory", String.valueOf(idCategory))
                .header(HttpHeaders.AUTHORIZATION, bearerToken);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Chocolate"))
                .andExpect(jsonPath("$[0].description").value("Delicious"))
                .andExpect(jsonPath("$[0].price").value(123456L))
                .andExpect(jsonPath("$[0].imageUrl").value("https://picsum.photos/200"));
    }
}