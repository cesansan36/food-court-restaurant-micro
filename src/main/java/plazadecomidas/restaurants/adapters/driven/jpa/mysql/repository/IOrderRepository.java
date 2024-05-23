package plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.OrderEntity;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
}
