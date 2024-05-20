package plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter;

import lombok.RequiredArgsConstructor;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.MealEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryAlreadyExistsException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IMealEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IMealRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.util.PersistenceConstants;
import plazadecomidas.restaurants.domain.model.Meal;
import plazadecomidas.restaurants.domain.secondaryport.IMealPersistencePort;

@RequiredArgsConstructor
public class MealAdapter implements IMealPersistencePort {

    private final IMealRepository mealRepository;
    private final IMealEntityMapper mealEntityMapper;

    @Override
    public void saveMeal(Meal meal) {

        mealRepository.findByName(meal.getName()).ifPresent(mealEntity -> {
            throw new RegistryAlreadyExistsException(PersistenceConstants.MEAL_ALREADY_EXISTS_MESSAGE);
        });

        MealEntity mealEntity = mealEntityMapper.toEntity(meal);
        mealRepository.save(mealEntity);
    }
}
