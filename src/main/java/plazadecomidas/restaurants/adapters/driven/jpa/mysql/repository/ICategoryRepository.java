package plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.CategoryEntity;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
