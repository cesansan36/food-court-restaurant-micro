package plazadecomidas.restaurants.domain.model;

public class MealOrder {

    private final Long idMeal;
    private final Long idOrder;
    private final Integer quantity;

    public MealOrder(Long idMeal, Long idOrder, Integer quantity) {
        this.idMeal = idMeal;
        this.idOrder = idOrder;
        this.quantity = quantity;
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
