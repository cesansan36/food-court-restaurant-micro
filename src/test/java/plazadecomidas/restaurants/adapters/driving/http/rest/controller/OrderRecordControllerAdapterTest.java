package plazadecomidas.restaurants.adapters.driving.http.rest.controller;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import plazadecomidas.restaurants.TestData.ControllerTestData;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.response.RecordResponse;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IAddOrderRecordRequestMapper;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IOrderRecordResponseMapper;
import plazadecomidas.restaurants.domain.model.OrderRecord;
import plazadecomidas.restaurants.domain.primaryport.IOrderRecordPrimaryPort;
import plazadecomidas.restaurants.util.ITokenUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
class OrderRecordControllerAdapterTest {

    @InjectMocks private OrderRecordControllerAdapter orderRecordControllerAdapter;

    @Mock IOrderRecordPrimaryPort orderRecordPrimaryPort;
    @Mock IAddOrderRecordRequestMapper addOrderRecordRequestMapper;
    @Mock IOrderRecordResponseMapper orderRecordResponseMapper;
    @Mock ITokenUtils tokenUtils;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(orderRecordControllerAdapter).build();
    }

    @Test
    void list() throws Exception {
        String bearerToken = "bearerToken";
        Long userId = 1L;
        Claim claim = ControllerTestData.getIdClaim(userId);
        OrderRecord orderRecord = mock(OrderRecord.class);
        RecordResponse recordResponse = new RecordResponse(
                1L,
                1L,
                "client@email.com",
                LocalDateTime.now(),
                "PENDING",
                "READY",
                1L,
                "employee@email.com"
        );

        when(tokenUtils.validateToken(anyString())).thenReturn(mock(DecodedJWT.class));
        when(tokenUtils.getSpecificClaim(any(DecodedJWT.class), any(String.class))).thenReturn(claim);
        when(orderRecordPrimaryPort.listRecords(anyString(), anyLong())).thenReturn(List.of(orderRecord));
        when(orderRecordResponseMapper.recordsToRecordResponses(anyList())).thenReturn(List.of(recordResponse));

        MockHttpServletRequestBuilder request = get("/records/listRecords")
                                                .header("Authorization", bearerToken);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()));

        verify(orderRecordPrimaryPort, times(1)).listRecords(anyString(), anyLong());
        verify(orderRecordResponseMapper, times(1)).recordsToRecordResponses(anyList());
        verify(tokenUtils, times(1)).validateToken(anyString());
        verify(tokenUtils, times(1)).getSpecificClaim(any(DecodedJWT.class), any(String.class));
    }
}