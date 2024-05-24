package plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter;

import lombok.AllArgsConstructor;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IEmployeeEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IEmployeeRepository;
import plazadecomidas.restaurants.domain.model.Employee;
import plazadecomidas.restaurants.domain.secondaryport.IEmployeePersistencePort;

@AllArgsConstructor
public class EmployeeAdapter implements IEmployeePersistencePort {

    private final IEmployeeRepository employeeRepository;
    private final IEmployeeEntityMapper employeeEntityMapper;

    @Override
    public void saveEmployee(Employee employee) {
        employeeRepository.save(employeeEntityMapper.employeeToEmployeeEntity(employee));
    }
}
