package plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meal")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MealEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Long price;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "is_active")
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "id_restaurant")
    private RestaurantEntity restaurant;
    @ManyToOne
    @JoinColumn(name = "id_category")
    private CategoryEntity category;

    @OneToMany(mappedBy = "meal")
    private List<OrderMealEntity> orders = new ArrayList<>();
}
