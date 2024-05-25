package plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeEntity {

    @Id
    @Column(name = "id")
    private Long idEmployee;
    @Column(name = "id_restaurant")
    private Long idRestaurant;
}
