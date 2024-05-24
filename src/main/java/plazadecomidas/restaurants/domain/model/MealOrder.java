package plazadecomidas.restaurants.domain.model;

import plazadecomidas.restaurants.domain.exception.EmptyFieldException;
import plazadecomidas.restaurants.domain.exception.FieldRuleInvalidException;
import plazadecomidas.restaurants.domain.util.DomainConstants;

public class MealOrder {

    private final Long idMeal;
    private final Long idOrder;
    private final Integer quantity;

    public MealOrder(Long idMeal, Long idOrder, Integer quantity) {

        validateData(idMeal, quantity);

        this.idMeal = idMeal;
        this.idOrder = idOrder;
        this.quantity = quantity;
    }

    void validateData(Long idMeal, Integer quantity) {
        if(idMeal == null) throw new EmptyFieldException(DomainConstants.MealOrderFields.ID_MEAL.name());
        if(quantity == null) throw new EmptyFieldException(DomainConstants.MealOrderFields.QUANTITY.name());
        if(quantity <= 0) throw new FieldRuleInvalidException(DomainConstants.QUANTITY_NEGATIVE_MESSAGE);
    }

    public Long getIdMeal() {
        return idMeal;
    }

    public Long getIdOrder() {
        return idOrder;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
