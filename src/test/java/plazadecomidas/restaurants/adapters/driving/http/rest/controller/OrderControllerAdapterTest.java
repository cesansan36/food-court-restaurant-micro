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
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.AddOrderRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IOrderRequestMapper;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IOrderResponseMapper;
import plazadecomidas.restaurants.domain.model.Order;
import plazadecomidas.restaurants.domain.primaryport.IOrderServicePort;
import plazadecomidas.restaurants.util.ITokenUtils;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerAdapterTest {

    private OrderControllerAdapter orderControllerAdapter;

    private IOrderServicePort orderServicePort;
    private IOrderRequestMapper orderRequestMapper;
    private IOrderResponseMapper orderResponseMapper;
    private ITokenUtils tokenUtils;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        orderServicePort = mock(IOrderServicePort.class);
        orderRequestMapper = mock(IOrderRequestMapper.class);
        orderResponseMapper = mock(IOrderResponseMapper.class);
        tokenUtils = mock(ITokenUtils.class);
        orderControllerAdapter = new OrderControllerAdapter(orderServicePort, orderRequestMapper, orderResponseMapper, tokenUtils);
        mockMvc = MockMvcBuilders.standaloneSetup(orderControllerAdapter).build();
    }

    @Test
    void registerOrder() throws Exception {

        Object inputObject = new Object() {
            public final Long restaurantId = 1L;
            public final List<Object> mealsInOrder = List.of(new Object() {
                public final Long mealId = 1L;
                public final Integer quantity = 1;
            });
        };
        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(inputObject);

        DecodedJWT decodedToken = mock(DecodedJWT.class);
        Claim claim = ControllerTestData.getIdClaim(1L);
        String bearerToken = "Bearer 123456789";
        Order order = DomainTestData.getValidOrder(1L);

        when(tokenUtils.validateToken(anyString())).thenReturn(decodedToken);
        when(tokenUtils.getSpecificClaim(any(DecodedJWT.class), anyString())).thenReturn(claim);
        when(orderRequestMapper.addOrderRequestToOrder(any(AddOrderRequest.class), anyLong(), anyString())).thenReturn(order);

        MockHttpServletRequestBuilder request = post("/orders/register")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated());

        verify(tokenUtils, times(1)).getSpecificClaim(any(DecodedJWT.class), anyString());
        verify(tokenUtils, times(1)).validateToken(anyString());
        verify(orderRequestMapper, times(1)).addOrderRequestToOrder(any(AddOrderRequest.class), anyLong(), anyString());
        verify(orderServicePort, times(1)).saveOrder(any(Order.class));
    }
}