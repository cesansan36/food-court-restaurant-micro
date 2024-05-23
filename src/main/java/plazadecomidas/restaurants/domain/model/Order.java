package plazadecomidas.restaurants.domain.model;

import java.time.LocalDate;
import java.util.List;

public class Order {

    private final Long id;
    private final Long idClient;
    private final LocalDate date;
    private final String status;
    private final Long idChef;
    private final Long idRestaurant;
    private final List<MealOrder> meals;

    public Order(Long id, Long idClient, LocalDate date, String status, Long idChef, Long idRestaurant, List<MealOrder> meals) {
        this.id = id;
        this.idClient = idClient;
        this.date = date;
        this.status = status;
        this.idChef = idChef;
        this.idRestaurant = idRestaurant;
        this.meals = meals;
    }

    public Long getId() {
        return id;
    }

    public Long getIdClient() {
        return idClient;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public Long getIdChef() {
        return idChef;
    }

    public Long getIdRestaurant() {
        return idRestaurant;
    }

    public List<MealOrder> getMeals() {
        return meals;
    }
}
