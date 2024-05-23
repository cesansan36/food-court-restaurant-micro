package plazadecomidas.restaurants.domain.model;

import java.time.LocalDate;
import java.util.List;

public class Order {

    private final Long id;
    private final Long clientId;
    private final LocalDate date;
    private final String status;
    private final Long idChef;
    private final Long idRestaurant;
    private final List<Meal> meals;

    public Order(Long id, Long clientId, LocalDate date, String status, Long idChef, Long idRestaurant, List<Meal> meals) {
        this.id = id;
        this.clientId = clientId;
        this.date = date;
        this.status = status;
        this.idChef = idChef;
        this.idRestaurant = idRestaurant;
        this.meals = meals;
    }

    public Long getId() {
        return id;
    }

    public Long getClientId() {
        return clientId;
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

    public List<Meal> getMeals() {
        return meals;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", idChef=" + idChef +
                ", idRestaurant=" + idRestaurant +
                ", meals=" + meals +
                '}';
    }
}
