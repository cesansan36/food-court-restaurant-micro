package plazadecomidas.restaurants.adapters.driving.http.rest.controller;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import plazadecomidas.restaurants.TestData.ControllerTestData;
import plazadecomidas.restaurants.TestData.DomainTestData;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.AddOrderRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.UpdateOrderRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.UpdateOrderToDeliveredRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.response.OrderCancelledResponse;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.response.OrderResponse;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IOrderCancelledResponseMapper;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IOrderRequestMapper;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IOrderResponseMapper;
import plazadecomidas.restaurants.domain.model.OperationResult;
import plazadecomidas.restaurants.domain.model.Order;
import plazadecomidas.restaurants.domain.primaryport.IOrderServicePort;
import plazadecomidas.restaurants.util.ITokenUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerAdapterTest {

    private OrderControllerAdapter orderControllerAdapter;

    private IOrderServicePort orderServicePort;
    private IOrderRequestMapper orderRequestMapper;
    private IOrderResponseMapper orderResponseMapper;
    private IOrderCancelledResponseMapper orderCancelledResponseMapper;
    private ITokenUtils tokenUtils;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        orderServicePort = mock(IOrderServicePort.class);
        orderRequestMapper = mock(IOrderRequestMapper.class);
        orderCancelledResponseMapper = mock(IOrderCancelledResponseMapper.class);
        orderResponseMapper = mock(IOrderResponseMapper.class);
        tokenUtils = mock(ITokenUtils.class);
        orderControllerAdapter = new OrderControllerAdapter(orderServicePort, orderRequestMapper, orderResponseMapper, orderCancelledResponseMapper, tokenUtils);
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

    @ParameterizedTest
    @CsvSource({
            "0, 5, PENDING",
            "-11, 32, DELIVERED",
            "2, -60, CANCELLED"
    })
    void listOrders(Integer page, Integer size, String status) throws Exception {

        String bearerToken = "Bearer 123456789";
        Claim claim = ControllerTestData.getIdClaim(1L);
        Order order = DomainTestData.getValidOrder(1L);
        OrderResponse orderResponse = mock(OrderResponse.class);

        when(tokenUtils.validateToken(anyString())).thenReturn(mock(DecodedJWT.class));
        when(tokenUtils.getSpecificClaim(any(DecodedJWT.class), anyString())).thenReturn(claim);
        when(orderResponseMapper.ordersToOrderResponses(anyList())).thenReturn(List.of(orderResponse));
        when(orderServicePort.getOrdersByStatus(anyLong(), anyInt(), anyInt(), anyString())).thenReturn(List.of(order));

        MockHttpServletRequestBuilder request = get("/orders/listRecords")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("status", status);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        verify(tokenUtils, times(1)).getSpecificClaim(any(DecodedJWT.class), anyString());
        verify(tokenUtils, times(1)).validateToken(anyString());
        verify(orderResponseMapper, times(1)).ordersToOrderResponses(anyList());
        verify(orderServicePort, times(1)).getOrdersByStatus(anyLong(), anyInt(), anyInt(), anyString());
    }

    @Test
    void assignChef() throws Exception {

        Object objectInput = new Object() {
            public final Long id = 1L;
        };

        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(objectInput);

        String bearerToken = "Bearer 123456789";
        Claim claim = ControllerTestData.getIdClaim(1L);
        Order order = DomainTestData.getValidOrder(1L);

        when(tokenUtils.validateToken(anyString())).thenReturn(mock(DecodedJWT.class));
        when(tokenUtils.getSpecificClaim(any(DecodedJWT.class), anyString())).thenReturn(claim);
        when(orderRequestMapper.updateOrderRequestToOrder(any(UpdateOrderRequest.class), anyLong(), anyString())).thenReturn(order);

        MockHttpServletRequestBuilder request = put("/orders/assign_chef")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        verify(tokenUtils, times(1)).getSpecificClaim(any(DecodedJWT.class), anyString());
        verify(tokenUtils, times(1)).validateToken(anyString());
        verify(orderRequestMapper, times(1)).updateOrderRequestToOrder(any(UpdateOrderRequest.class), anyLong(), anyString());
        verify(orderServicePort, times(1)).updateOrderPreparing(any(Order.class));
    }

    @Test
    void setReady() throws Exception {
        Object inputObject = new Object() {
            public final Long id = 1L;
        };
        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(inputObject);

        Claim claim = ControllerTestData.getIdClaim(1L);
        Order order = DomainTestData.getValidOrder(1L);
        String token = "Bearer token";

        when(tokenUtils.validateToken(anyString())).thenReturn(mock(DecodedJWT.class));
        when(tokenUtils.getSpecificClaim(any(DecodedJWT.class), anyString())).thenReturn(claim);
        when(orderRequestMapper.updateOrderRequestToOrder(any(UpdateOrderRequest.class), anyLong(), anyString())).thenReturn(order);

        MockHttpServletRequestBuilder request = put("/orders/set_ready")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        verify(tokenUtils, times(1)).validateToken(anyString());
        verify(orderRequestMapper, times(1)).updateOrderRequestToOrder(any(UpdateOrderRequest.class), anyLong(), anyString());
        verify(orderServicePort, times(1)).updateOrderReady(any(Order.class), anyString());
    }

    @Test
    void setDelivered() throws Exception {

        Object inputObject = new Object() {
            public final Long id = 1L;
            public final Integer securityPin = 1234;
        };
        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(inputObject);

        Order order = DomainTestData.getValidOrder(1L);

        when(orderRequestMapper.updateOrderToDeliveredRequestToOrder(any(UpdateOrderToDeliveredRequest.class), anyString())).thenReturn(order);

        MockHttpServletRequestBuilder request = put("/orders/set_delivered")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        verify(orderRequestMapper, times(1)).updateOrderToDeliveredRequestToOrder(any(UpdateOrderToDeliveredRequest.class), anyString());
        verify(orderServicePort, times(1)).updateOrderDelivered(any(Order.class));
    }

    @Test
    void cancelOrderSuccess() throws Exception {
        Object inputObject = new Object() {
            public final Long id = 1L;
        };
        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(inputObject);

        String bearerToken = "Bearer 123456789";
        Claim claim = ControllerTestData.getIdClaim(1L);
        Order order = DomainTestData.getValidOrder(1L);
        OperationResult operationResult = new OperationResult(true, "Order cancelled successfully");
        OrderCancelledResponse operationResponse = new OrderCancelledResponse(operationResult.isSuccess(), operationResult.getMessage());

        when(tokenUtils.validateToken(anyString())).thenReturn(mock(DecodedJWT.class));
        when(tokenUtils.getSpecificClaim(any(DecodedJWT.class), anyString())).thenReturn(claim);
        when(orderRequestMapper.updateOrderCancelledRequestToOrder(any(UpdateOrderRequest.class), anyLong(), anyString())).thenReturn(order);
        when(orderServicePort.updateOrderCancelled(any(Order.class))).thenReturn(operationResult);
        when(orderCancelledResponseMapper.toCancelledResponse(any(OperationResult.class))).thenReturn(operationResponse);

        MockHttpServletRequestBuilder request = put("/orders/cancel")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        MvcResult result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        verify(tokenUtils, times(1)).getSpecificClaim(any(DecodedJWT.class), anyString());
        verify(tokenUtils, times(1)).validateToken(anyString());
        verify(orderRequestMapper, times(1)).updateOrderCancelledRequestToOrder(any(UpdateOrderRequest.class), anyLong(), anyString());
        verify(orderServicePort, times(1)).updateOrderCancelled(any(Order.class));
        verify(orderCancelledResponseMapper, times(1)).toCancelledResponse(any(OperationResult.class));

        String response = result.getResponse().getContentAsString();
        assertEquals(operationResponse.message(), response);
    }

    @Test
    void cancelOrderFailure() throws Exception {
        Object inputObject = new Object() {
            public final Long id = 1L;
        };
        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(inputObject);

        String bearerToken = "Bearer 123456789";
        Claim claim = ControllerTestData.getIdClaim(1L);
        Order order = DomainTestData.getValidOrder(1L);
        OperationResult operationResult = new OperationResult(false, "Order cancellation failed");
        OrderCancelledResponse operationResponse = new OrderCancelledResponse(operationResult.isSuccess(), operationResult.getMessage());

        when(tokenUtils.validateToken(anyString())).thenReturn(mock(DecodedJWT.class));
        when(tokenUtils.getSpecificClaim(any(DecodedJWT.class), anyString())).thenReturn(claim);
        when(orderRequestMapper.updateOrderCancelledRequestToOrder(any(UpdateOrderRequest.class), anyLong(), anyString())).thenReturn(order);
        when(orderServicePort.updateOrderCancelled(any(Order.class))).thenReturn(operationResult);
        when(orderCancelledResponseMapper.toCancelledResponse(any(OperationResult.class))).thenReturn(operationResponse);

        MockHttpServletRequestBuilder request = put("/orders/cancel")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        MvcResult result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();


        verify(tokenUtils, times(1)).getSpecificClaim(any(DecodedJWT.class), anyString());
        verify(tokenUtils, times(1)).validateToken(anyString());
        verify(orderRequestMapper, times(1)).updateOrderCancelledRequestToOrder(any(UpdateOrderRequest.class), anyLong(), anyString());
        verify(orderServicePort, times(1)).updateOrderCancelled(any(Order.class));
        verify(orderCancelledResponseMapper, times(1)).toCancelledResponse(any(OperationResult.class));

        String response = result.getResponse().getContentAsString();
        assertEquals(operationResponse.message(), response);
    }
}