package plazadecomidas.restaurants.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeTest {

    @Test
    void createEmployeeCorrectly() {
        Long idEmployee = 1L;
        Long idRestaurant = 1L;

        Employee employee = new Employee(idEmployee, idRestaurant);

        assertEquals(idEmployee, employee.getIdEmployee());
        assertEquals(idRestaurant, employee.getIdRestaurant());
    }

}