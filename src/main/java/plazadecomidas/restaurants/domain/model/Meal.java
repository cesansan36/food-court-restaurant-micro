package plazadecomidas.restaurants.domain.model;

import plazadecomidas.restaurants.domain.exception.EmptyFieldException;
import plazadecomidas.restaurants.domain.exception.FieldRuleInvalidException;
import plazadecomidas.restaurants.domain.util.DomainConstants;
import plazadecomidas.restaurants.domain.util.Validator;

public class Meal {

    private final Long id;
    private final String name;
    private final String description;
    private final Long price;
    private final String imageUrl;
    private final boolean isActive;
    private final Restaurant restaurant;
    private final Category category;

    public Meal(Long id, String name, String description, Long price, String imageUrl, boolean isActive, Restaurant restaurant, Category category) {

        validateData(name, description, price, imageUrl, restaurant, category);

        name = name.trim();
        description = description.trim();
        imageUrl = imageUrl.trim();

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
        this.restaurant = restaurant;
        this.category = category;
    }

    private void validateData(String name, String description, Long price, String imageUrl, Restaurant restaurant, Category category) {
        if (Validator.isFieldEmpty(name)) {
            throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.MealFields.NAME));
        }

        if (Validator.isFieldEmpty(description)) {
            throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.MealFields.DESCRIPTION));
        }

        if (Validator.isFieldEmpty(imageUrl)) {
            throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.MealFields.IMAGE_URL));
        }

        if (price == null) {
            throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.MealFields.PRICE));
        }

        if (restaurant == null) {
            throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.MealFields.RESTAURANT));
        }

        if (category == null) {
            throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.MealFields.CATEGORY));
        }

        if(!Validator.isValidPrice(price)) {
            throw new FieldRuleInvalidException(DomainConstants.PRICE_NOT_VALID);
        }

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Category getCategory() {
        return category;
    }
}
