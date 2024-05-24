package plazadecomidas.restaurants.domain.primaryport;

import plazadecomidas.restaurants.domain.model.Employee;

public interface IEmployeeServicePort {
    void save(Employee employee);
}
