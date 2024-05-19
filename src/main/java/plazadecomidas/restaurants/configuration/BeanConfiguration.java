package plazadecomidas.restaurants.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import plazadecomidas.restaurants.adapters.driven.feign.IUserFeignClient;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter.RestaurantAdapter;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IRestaurantEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IRestaurantRepository;
import plazadecomidas.restaurants.domain.primaryport.IRestaurantServicePort;
import plazadecomidas.restaurants.domain.primaryport.usecase.RestaurantUseCase;
import plazadecomidas.restaurants.domain.secondaryport.IRestaurantPersistencePort;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final IUserFeignClient userFeignClient;

    @Bean
    public IRestaurantServicePort restaurantServicePort() {
        return new RestaurantUseCase(restaurantPersistencePort(), userFeignClient);
    }

    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort() {
        return new RestaurantAdapter(restaurantRepository, restaurantEntityMapper);
    }
}
