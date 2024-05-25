package plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.RestaurantEntity;

import java.util.Optional;

public interface IRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    Optional<RestaurantEntity> findByAddress(String address);

    Optional<RestaurantEntity> findByIdAndOwnerId(Long id, Long ownerId);

    @Query("SELECT COUNT(r) > 0 FROM RestaurantEntity r WHERE r.ownerId = :ownerId AND r.id = :restaurantId")
    boolean existsByOwnerIdAndId(@Param("ownerId") Long ownerId, @Param("restaurantId") Long restaurantId);
}
