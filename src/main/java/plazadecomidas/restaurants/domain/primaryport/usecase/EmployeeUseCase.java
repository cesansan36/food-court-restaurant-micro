package plazadecomidas.restaurants.domain.primaryport.usecase;

import plazadecomidas.restaurants.domain.exception.DataMismatchException;
import plazadecomidas.restaurants.domain.model.Employee;
import plazadecomidas.restaurants.domain.primaryport.IEmployeeServicePort;
import plazadecomidas.restaurants.domain.secondaryport.IEmployeePersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IRestaurantPersistencePort;
import plazadecomidas.restaurants.domain.util.DomainConstants;

public class EmployeeUseCase implements IEmployeeServicePort {

    private final IEmployeePersistencePort employeePersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    public EmployeeUseCase(IEmployeePersistencePort employeePersistencePort, IRestaurantPersistencePort restaurantPersistencePort) {
        this.employeePersistencePort = employeePersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public void save(Employee employee, Long idOwner) {

        boolean restaurantExists = restaurantPersistencePort.existsOwnerRestaurantMatch(idOwner, employee.getIdRestaurant());

        if (!restaurantExists) {
            throw new DataMismatchException(DomainConstants.RESTAURANT_OWNER_MISMATCH_MESSAGE);
        }

        employeePersistencePort.saveEmployee(employee);
    }
}
