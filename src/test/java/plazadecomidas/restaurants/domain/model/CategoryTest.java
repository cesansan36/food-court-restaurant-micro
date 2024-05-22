package plazadecomidas.restaurants.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void createCategory() {
        Category category = new Category(1L, "Category 1", "Description 1");
        assertAll(
            () -> assertEquals(1L, category.getId()),
            () -> assertEquals("Category 1", category.getName()),
            () -> assertEquals("Description 1", category.getDescription())
        );
    }

}