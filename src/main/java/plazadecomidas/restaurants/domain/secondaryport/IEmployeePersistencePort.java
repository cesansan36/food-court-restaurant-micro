package plazadecomidas.restaurants.domain.secondaryport;

import plazadecomidas.restaurants.domain.model.Employee;

public interface IEmployeePersistencePort {
    void saveEmployee(Employee employee);

    Long getRestaurantIdByEmployeeId(Long idEmployee);
}
