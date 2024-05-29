package plazadecomidas.restaurants.domain.model;

import java.time.LocalDateTime;

public class OrderRecord {

    private final String id;
    private final Long idOrder;
    private final Long idClient;
    private final String clientEmail;
    private final LocalDateTime createdAt;
    private final String previousState;
    private final String newState;
    private final Long idEmployee;
    private final String employeeEmail;

    public OrderRecord(String id, Long idOrder, Long idClient, String clientEmail, LocalDateTime createdAt, String previousState, String newState, Long idEmployee, String employeeEmail) {
        this.id = id;
        this.idOrder = idOrder;
        this.idClient = idClient;
        this.clientEmail = clientEmail;
        this.createdAt = createdAt;
        this.previousState = previousState;
        this.newState = newState;
        this.idEmployee = idEmployee;
        this.employeeEmail = employeeEmail;
    }

    public String getId() {
        return id;
    }

    public Long getIdOrder() {
        return idOrder;
    }

    public Long getIdClient() {
        return idClient;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getPreviousState() {
        return previousState;
    }

    public String getNewState() {
        return newState;
    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }
}
