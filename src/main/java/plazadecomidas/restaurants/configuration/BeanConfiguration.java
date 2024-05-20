package plazadecomidas.restaurants.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import plazadecomidas.restaurants.adapters.driven.feign.IUserFeignClient;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter.MealAdapter;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter.RestaurantAdapter;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IMealEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IRestaurantEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IMealRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IRestaurantRepository;
import plazadecomidas.restaurants.domain.primaryport.IMealServicePort;
import plazadecomidas.restaurants.domain.primaryport.IRestaurantServicePort;
import plazadecomidas.restaurants.domain.primaryport.usecase.MealUseCase;
import plazadecomidas.restaurants.domain.primaryport.usecase.RestaurantUseCase;
import plazadecomidas.restaurants.domain.secondaryport.IMealPersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IRestaurantPersistencePort;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IRestaurantRepository restaurantRepository;
    private final IMealRepository mealRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final IMealEntityMapper mealEntityMapper;
    private final IUserFeignClient userFeignClient;

    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort() {
        return new RestaurantAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort() {
        return new RestaurantUseCase(restaurantPersistencePort(), userFeignClient);
    }

    @Bean
    public IMealPersistencePort mealPersistencePort() {
        return new MealAdapter(mealRepository, mealEntityMapper);
    }

    @Bean
    public IMealServicePort mealServicePort() {
        return new MealUseCase(mealPersistencePort());
    }
}
