package plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_meal")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderMealEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_meal")
    private MealEntity meal;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_order")
    private OrderEntity order;

    private int amount;
}
