package plazadecomidas.restaurants.domain.primaryport.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plazadecomidas.restaurants.domain.exception.DataMismatchException;
import plazadecomidas.restaurants.domain.model.Employee;
import plazadecomidas.restaurants.domain.secondaryport.IEmployeePersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IRestaurantPersistencePort;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeUseCaseTest {

    private EmployeeUseCase employeeUseCase;

    private IEmployeePersistencePort employeePersistencePort;
    private IRestaurantPersistencePort restaurantPersistencePort;

    @BeforeEach
    void setUp() {
        employeePersistencePort = mock(IEmployeePersistencePort.class);
        restaurantPersistencePort = mock(IRestaurantPersistencePort.class);
        employeeUseCase = new EmployeeUseCase(employeePersistencePort, restaurantPersistencePort);
    }

    @Test
    void SaveEmployeeCorrectly() {
        Employee employee = new Employee(1L, 1L);
        Long idOwner = 1L;

        when(restaurantPersistencePort.existsOwnerRestaurantMatch(anyLong(), anyLong())).thenReturn(true);

        employeeUseCase.save(employee, idOwner);

        verify(restaurantPersistencePort, times(1)).existsOwnerRestaurantMatch(anyLong(), anyLong());
        verify(employeePersistencePort, times(1)).saveEmployee(employee);
    }

    @Test
    void SaveEmployeeFailBecauseRestaurantOwnerMismatch() {
        Employee employee = new Employee(1L, 1L);
        Long idOwner = 1L;

        when(restaurantPersistencePort.existsOwnerRestaurantMatch(anyLong(), anyLong())).thenReturn(false);

        assertThrows(DataMismatchException.class, () -> employeeUseCase.save(employee, idOwner));

        verify(restaurantPersistencePort, times(1)).existsOwnerRestaurantMatch(anyLong(), anyLong());
        verify(employeePersistencePort, times(0)).saveEmployee(employee);
    }
}