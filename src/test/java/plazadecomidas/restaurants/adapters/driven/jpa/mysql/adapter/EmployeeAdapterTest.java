package plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.EmployeeEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IEmployeeEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IEmployeeRepository;
import plazadecomidas.restaurants.domain.model.Employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeAdapterTest {

    private EmployeeAdapter employeeAdapter;

    private IEmployeeRepository employeeRepository;
    private IEmployeeEntityMapper employeeEntityMapper;

    @BeforeEach
    void setUp() {
        employeeRepository = mock(IEmployeeRepository.class);
        employeeEntityMapper = mock(IEmployeeEntityMapper.class);
        employeeAdapter = new EmployeeAdapter(employeeRepository, employeeEntityMapper);
    }

    @Test
    void saveEmployee() {
        Employee employee = mock(Employee.class);
        EmployeeEntity employeeEntity = mock(EmployeeEntity.class);

        when(employeeEntityMapper.employeeToEmployeeEntity(any(Employee.class))).thenReturn(employeeEntity);

        employeeAdapter.saveEmployee(employee);

        verify(employeeRepository, times(1)).save(employeeEntity);
    }

    @Test
    void getRestaurantIdByEmployeeId() {
        Long idEmployee = 1L;
        Long idRestaurant = 1L;

        when(employeeRepository.findIdRestaurantByIdEmployee(any(Long.class))).thenReturn(idRestaurant);

        Long result = employeeAdapter.getRestaurantIdByEmployeeId(idEmployee);

        verify(employeeRepository, times(1)).findIdRestaurantByIdEmployee(idEmployee);
        assertEquals(idRestaurant, result);
    }
}