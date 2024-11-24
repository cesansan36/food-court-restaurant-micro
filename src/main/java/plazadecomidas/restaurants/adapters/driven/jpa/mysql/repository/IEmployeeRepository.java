package plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.EmployeeEntity;

public interface IEmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

    @Query("SELECT COUNT(e) > 0 FROM EmployeeEntity e WHERE e.idEmployee = :idEmployee AND e.idRestaurant = :idRestaurant")
    boolean existsByEmployeeIdAndRestaurantId(Long idEmployee, Long idRestaurant);

    @Query("SELECT e.idRestaurant FROM EmployeeEntity e WHERE e.idEmployee = :idEmployee")
    Long findIdRestaurantByIdEmployee(@Param("idEmployee") Long idEmployee);
}
