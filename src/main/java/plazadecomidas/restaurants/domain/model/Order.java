package plazadecomidas.restaurants.domain.model;

import plazadecomidas.restaurants.domain.exception.EmptyFieldException;
import plazadecomidas.restaurants.domain.exception.FieldRuleInvalidException;
import plazadecomidas.restaurants.domain.util.DomainConstants;
import plazadecomidas.restaurants.domain.util.Validator;

import java.time.LocalDate;
import java.util.List;

public class Order {

    private final Long id;
    private final Long idClient;
    private final LocalDate date;
    private final String status;
    private final Integer securityPin;
    private final Long idChef;
    private final Long idRestaurant;
    private final List<MealOrder> meals;

    public Order(Long id, Long idClient, LocalDate date, String status, Integer securityPin, Long idChef, Long idRestaurant, List<MealOrder> meals) {

        validateFields(idClient, date, status, idRestaurant, meals);

        status = status.trim();

        this.id = id;
        this.idClient = idClient;
        this.date = date;
        this.status = status;
        this.securityPin = securityPin;
        this.idChef = idChef;
        this.idRestaurant = idRestaurant;
        this.meals = meals;
    }

    void validateFields(Long idClient, LocalDate date, String status, Long idRestaurant, List<MealOrder> meals) {
        if(idClient == null) throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.OrderFields.ID_CLIENT.name()));
        if(date == null) throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.OrderFields.DATE.name()));
        if(Validator.isFieldEmpty(status)) throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.OrderFields.STATUS.name()));
        if(!DomainConstants.OrderStatus.isValidStatus(status)) throw new FieldRuleInvalidException(DomainConstants.WRONG_STATUS_MESSAGE);
        if(idRestaurant == null) throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.OrderFields.ID_RESTAURANT.name()));
        if(meals == null) throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.OrderFields.MEALS.name()));
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

    public Integer getSecurityPin() {
        return securityPin;
    }
}
