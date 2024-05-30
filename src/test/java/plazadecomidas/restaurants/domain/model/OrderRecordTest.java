package plazadecomidas.restaurants.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderRecordTest {

    @Test
    void createOrderRecord() {
        LocalDateTime now = LocalDateTime.now();
        OrderRecord orderRecord = new OrderRecord(
                "id",
                1L,
                1L,
                "clientEmail",
                now,
                "previousState",
                "newState",
                1L,
                "employeeEmail");

        assertAll(
                () -> assertEquals("id", orderRecord.getId()),
                () -> assertEquals(1L, orderRecord.getIdOrder()),
                () -> assertEquals(1L, orderRecord.getIdClient()),
                () -> assertEquals("clientEmail", orderRecord.getClientEmail()),
                () -> assertEquals(now, orderRecord.getCreatedAt()),
                () -> assertEquals("previousState", orderRecord.getPreviousState()),
                () -> assertEquals("newState", orderRecord.getNewState()),
                () -> assertEquals(1L, orderRecord.getIdEmployee()),
                () -> assertEquals("employeeEmail", orderRecord.getEmployeeEmail())
        );
    }

}