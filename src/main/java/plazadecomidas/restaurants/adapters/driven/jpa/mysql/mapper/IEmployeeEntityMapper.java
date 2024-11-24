package plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper;

import org.mapstruct.Mapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.EmployeeEntity;
import plazadecomidas.restaurants.domain.model.Employee;

@Mapper(componentModel = "spring")
public interface IEmployeeEntityMapper {

    EmployeeEntity employeeToEmployeeEntity(Employee employee);
}
