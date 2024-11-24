package plazadecomidas.restaurants.adapters.driving.http.rest.controller;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import plazadecomidas.restaurants.TestData.ControllerTestData;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.AddEmployeeRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.exception.IdMismatchException;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IEmployeeRequestMapper;
import plazadecomidas.restaurants.configuration.exceptionhandler.ControllerAdvisor;
import plazadecomidas.restaurants.domain.model.Employee;
import plazadecomidas.restaurants.domain.primaryport.IEmployeeServicePort;
import plazadecomidas.restaurants.util.ITokenUtils;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
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

class EmployeeControllerAdapterTest {

    private EmployeeControllerAdapter employeeControllerAdapter;

    private IEmployeeServicePort employeeServicePort;
    private IEmployeeRequestMapper employeeRequestMapper;
    private ITokenUtils tokenUtils;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        employeeServicePort = mock(IEmployeeServicePort.class);
        employeeRequestMapper = mock(IEmployeeRequestMapper.class);
        tokenUtils = mock(ITokenUtils.class);
        employeeControllerAdapter = new EmployeeControllerAdapter(employeeServicePort, employeeRequestMapper, tokenUtils);

        mockMvc = MockMvcBuilders.standaloneSetup(employeeControllerAdapter).setControllerAdvice(new ControllerAdvisor()).build();
    }

    @Test
    void registerEmployeeSuccessful() throws Exception {

        Object inputObject = new Object () {
            public final Long idOwner = 1L;
            public final Long idEmployee = 1L;
            public final Long idRestaurant = 1L;
        };
        ObjectMapper mapper = new ObjectMapper();
        String inputJson = mapper.writeValueAsString(inputObject);

        String bearerToken = "Bearer token";
        Claim claim = ControllerTestData.getIdClaim(1L);

        when(tokenUtils.validateToken(anyString())).thenReturn(mock(DecodedJWT.class));
        when(tokenUtils.getSpecificClaim(any(DecodedJWT.class), anyString())).thenReturn(claim);
        when(employeeRequestMapper.addEmployeeRequestToEmployee(any(AddEmployeeRequest.class))).thenReturn(mock(Employee.class));

        MockHttpServletRequestBuilder request = post("/employees/register")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated());

        verify(employeeServicePort, times(1)).save(any(Employee.class), anyLong());
        verify(tokenUtils, times(1)).validateToken(anyString());
        verify(tokenUtils, times(1)).getSpecificClaim(any(DecodedJWT.class), anyString());
        verify(employeeRequestMapper, times(1)).addEmployeeRequestToEmployee(any(AddEmployeeRequest.class));
    }

    @Test
    @ExceptionHandler(IllegalArgumentException.class)
    void registerEmployeeFailBecauseOwnerMismatch() throws Exception {
        Object inputObject = new Object () {
            public final Long idOwner = 2L;
            public final Long idEmployee = 1L;
            public final Long idRestaurant = 1L;
        };
        ObjectMapper mapper = new ObjectMapper();
        String inputJson = mapper.writeValueAsString(inputObject);

        String bearerToken = "Bearer token";
        Claim claim = ControllerTestData.getIdClaim(1L);

        when(tokenUtils.validateToken(anyString())).thenReturn(mock(DecodedJWT.class));
        when(tokenUtils.getSpecificClaim(any(DecodedJWT.class), anyString())).thenReturn(claim);

        MockHttpServletRequestBuilder request = post("/employees/register")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(IdMismatchException.class, result.getResolvedException()));

        verify(tokenUtils, times(1)).validateToken(anyString());
        verify(tokenUtils, times(1)).getSpecificClaim(any(DecodedJWT.class), anyString());
        verify(employeeServicePort, times(0)).save(any(Employee.class), anyLong());
        verify(employeeRequestMapper, times(0)).addEmployeeRequestToEmployee(any(AddEmployeeRequest.class));
    }
}