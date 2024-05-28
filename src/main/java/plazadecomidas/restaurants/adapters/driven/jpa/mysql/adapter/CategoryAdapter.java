package plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter;

import lombok.RequiredArgsConstructor;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.CategoryEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryNotFoundException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.ICategoryRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.util.PersistenceConstants;
import plazadecomidas.restaurants.domain.secondaryport.ICategoryPersistencePort;

import java.util.Optional;

@RequiredArgsConstructor
public class CategoryAdapter implements ICategoryPersistencePort {

    private final ICategoryRepository categoryRepository;

    @Override
    public boolean existsCategory(Long idCategory) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(idCategory);
        if (categoryEntity.isEmpty()) {
            throw new RegistryNotFoundException(PersistenceConstants.CATEGORY_NOT_FOUND_MESSAGE);
        }
        return true;
    }
}
