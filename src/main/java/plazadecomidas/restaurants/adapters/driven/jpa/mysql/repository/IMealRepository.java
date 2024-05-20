package plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.MealEntity;

import java.util.Optional;

public interface IMealRepository extends JpaRepository<MealEntity, Long> {

    public Optional<MealEntity> findByName(String name);
}
