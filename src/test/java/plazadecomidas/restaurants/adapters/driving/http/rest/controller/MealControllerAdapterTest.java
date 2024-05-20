package plazadecomidas.restaurants.adapters.driving.http.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import plazadecomidas.restaurants.TestData.DomainTestData;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.AddMealRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.UpdateMealRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IMealRequestMapper;
import plazadecomidas.restaurants.domain.model.Meal;
import plazadecomidas.restaurants.domain.primaryport.IMealServicePort;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MealControllerAdapterTest {

    private MealControllerAdapter mealControllerAdapter;

    private IMealServicePort mealServicePort;
    private IMealRequestMapper mealRequestMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mealServicePort = mock(IMealServicePort.class);
        mealRequestMapper = mock(IMealRequestMapper.class);

        mealControllerAdapter = new MealControllerAdapter(mealServicePort, mealRequestMapper);

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

        when(mealRequestMapper.addMealRequestToMeal(any(AddMealRequest.class))).thenReturn(meal);

        MockHttpServletRequestBuilder request = post("/meals/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated());

        verify(mealRequestMapper, times(1)).addMealRequestToMeal(any(AddMealRequest.class));
        verify(mealServicePort, times(1)).saveMeal(meal);
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

        when(mealRequestMapper.updateMealRequestToMeal(any(UpdateMealRequest.class))).thenReturn(meal);

        MockHttpServletRequestBuilder request = put("/meals/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        verify(mealRequestMapper, times(1)).updateMealRequestToMeal(any(UpdateMealRequest.class));
        verify(mealServicePort, times(1)).updateMeal(meal);
    }
}