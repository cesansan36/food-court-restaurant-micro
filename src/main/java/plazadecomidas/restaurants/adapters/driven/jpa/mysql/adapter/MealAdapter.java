package plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter;

import lombok.RequiredArgsConstructor;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.MealEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryAlreadyExistsException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryNotFoundException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IMealEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IMealRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.util.PersistenceConstants;
import plazadecomidas.restaurants.domain.model.Meal;
import plazadecomidas.restaurants.domain.secondaryport.IMealPersistencePort;

import java.util.Optional;

@RequiredArgsConstructor
public class MealAdapter implements IMealPersistencePort {

    private final IMealRepository mealRepository;
    private final IMealEntityMapper mealEntityMapper;

    @Override
    public void saveMeal(Meal meal) {

        mealRepository.findByNameAndRestaurant_Id(meal.getName(), meal.getRestaurant().getId())
                .ifPresent(mealEntity -> {
            throw new RegistryAlreadyExistsException(PersistenceConstants.MEAL_ALREADY_EXISTS_MESSAGE);
        });

        MealEntity mealEntity = mealEntityMapper.toEntity(meal);
        mealRepository.save(mealEntity);
    }

    @Override
    public Meal getByName(String name) {
        Optional<MealEntity> mealFound = mealRepository.findByName(name);
        if (mealFound.isEmpty()) {
            throw new RegistryNotFoundException(PersistenceConstants.MEAL_NOT_FOUND_MESSAGE);
        }

        return mealEntityMapper.toDomain(mealFound.get());
    }

    @Override
    public Meal getByNameAndRestaurantId(String name, Long id) {
        return mealEntityMapper.toDomain(
                mealRepository.findByNameAndRestaurant_Id(name, id).orElseThrow(
                        () -> new RegistryNotFoundException(
                                PersistenceConstants.MEAL_NOT_FOUND_MESSAGE)));
    }

    @Override
    public void updateMeal(Meal meal) {
        Optional<MealEntity> foundMeal = mealRepository.findByNameAndRestaurant_Id(meal.getName(), meal.getRestaurant().getId());

        if (foundMeal.isEmpty()) {
            throw new RegistryNotFoundException(PersistenceConstants.MEAL_NOT_FOUND_MESSAGE);
        }

        mealRepository.save(mealEntityMapper.toEntity(meal));
    }
}
