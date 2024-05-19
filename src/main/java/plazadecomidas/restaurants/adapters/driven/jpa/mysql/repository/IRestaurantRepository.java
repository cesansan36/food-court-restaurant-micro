package plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.RestaurantEntity;

import java.util.Optional;

public interface IRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    public Optional<RestaurantEntity> findByAddress(String address);
}
