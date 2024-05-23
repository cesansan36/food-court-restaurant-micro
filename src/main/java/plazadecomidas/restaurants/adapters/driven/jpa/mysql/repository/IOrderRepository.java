package plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.OrderEntity;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.idClient = :clientId AND o.status IN ('PENDING', 'PREPARING', 'READY')")
    int countPendingOrPreparingOrReadyOrdersByClientId(@Param("clientId") Long clientId);
}
