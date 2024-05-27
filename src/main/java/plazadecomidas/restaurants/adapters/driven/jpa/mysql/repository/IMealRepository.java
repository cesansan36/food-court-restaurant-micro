package plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.MealEntity;

import java.util.List;
import java.util.Optional;

public interface IMealRepository extends JpaRepository<MealEntity, Long> {

    Optional<MealEntity> findByName(String name);
    Optional<MealEntity> findByNameAndRestaurant_Id(String name, Long restaurantId);

    List<MealEntity> findByActiveTrueAndRestaurantId(Long restaurantId);

    @Query("SELECT m FROM MealEntity m " +
            "WHERE m.restaurant.id = :restaurantId " +
            "AND m.category.id = :categoryId " +
            "AND m.active = true")
    Page<MealEntity> findActiveMealsByRestaurantAndCategory(Long restaurantId, Long categoryId, Pageable pageable);

    // Page<OrderEntity> findAllByRestaurantIdAndStatus(Pageable pageable, Long restaurantId, String status);

    List<Long> findAllRestaurantIdsByIdIn(List<Long> ids);

    @Query("SELECT m.restaurant.id FROM MealEntity m WHERE m.id IN :mealIds")
    List<Long> findRestaurantIdsByMealIds(@Param("mealIds") List<Long> mealIds);
}
