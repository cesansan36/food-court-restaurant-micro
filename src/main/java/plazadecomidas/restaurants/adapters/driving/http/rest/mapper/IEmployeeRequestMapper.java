package plazadecomidas.restaurants.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.AddEmployeeRequest;
import plazadecomidas.restaurants.domain.model.Employee;

@Mapper(componentModel = "spring")
public interface IEmployeeRequestMapper {

    Employee addEmployeeRequestToEmployee(AddEmployeeRequest addEmployeeRequest);
}
