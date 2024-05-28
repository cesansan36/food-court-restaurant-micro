package plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.CategoryEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryNotFoundException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.ICategoryRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CategoryAdapterTest {

    private CategoryAdapter categoryAdapter;

    private ICategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(ICategoryRepository.class);
        categoryAdapter = new CategoryAdapter(categoryRepository);
    }

    @Test
    void existsCategory() {
        Long idCategory = 1L;
        CategoryEntity categoryEntity = mock(CategoryEntity.class);

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryEntity));

        categoryAdapter.existsCategory(idCategory);

        verify(categoryRepository, times(1)).findById(anyLong());
    }

    @Test
    void existCategoryThrowBecauseNotExists() {
        Long idCategory = 1L;

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RegistryNotFoundException.class, () -> categoryAdapter.existsCategory(idCategory));

        verify(categoryRepository, times(1)).findById(anyLong());
    }
}