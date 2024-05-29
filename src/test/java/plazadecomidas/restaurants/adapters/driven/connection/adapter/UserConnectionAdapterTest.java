package plazadecomidas.restaurants.adapters.driven.connection.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import plazadecomidas.restaurants.adapters.driven.connection.feign.IUserFeignClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserConnectionAdapterTest {

    private UserConnectionAdapter userConnectionAdapter;

    private IUserFeignClient userFeignClient;

    @BeforeEach
    void setUp() {
        userFeignClient = mock(IUserFeignClient.class);
        userConnectionAdapter = new UserConnectionAdapter(userFeignClient);
    }

    @Test
    void verifyRole() {
        Long idClient = 1L;
        String role = "admin";

        when(userFeignClient.verifyRole(anyLong(), anyString())).thenReturn(ResponseEntity.ok(true));

        boolean result = userConnectionAdapter.verifyRole(idClient, role);

        assertTrue(result);
        verify(userFeignClient, times(1)).verifyRole(idClient, role);
    }

    @Test
    void getUserPhoneNumber() {
        Long idClient = 1L;
        String token = "Bearer token";

        when(userFeignClient.getNumber(anyString(), anyLong())).thenReturn(ResponseEntity.ok("123456789"));

        String result = userConnectionAdapter.getUserPhoneNumber(idClient, token);

        assertEquals("123456789", result);
        verify(userFeignClient, times(1)).getNumber(token, idClient);
    }
}