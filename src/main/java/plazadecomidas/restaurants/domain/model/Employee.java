package plazadecomidas.restaurants.domain.model;

public class Employee {

    private final Long idEmployee;
    private final Long idRestaurant;

    public Employee(Long idEmployee, Long idRestaurant) {
        this.idEmployee = idEmployee;
        this.idRestaurant = idRestaurant;
    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public Long getIdRestaurant() {
        return idRestaurant;
    }
}
