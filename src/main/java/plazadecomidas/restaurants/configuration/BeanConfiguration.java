package plazadecomidas.restaurants.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import plazadecomidas.restaurants.adapters.driven.connection.adapter.MessagingAdapter;
import plazadecomidas.restaurants.adapters.driven.connection.adapter.UserAdapter;
import plazadecomidas.restaurants.adapters.driven.connection.feign.IMessagingFeignClient;
import plazadecomidas.restaurants.adapters.driven.connection.feign.IUserFeignClient;
import plazadecomidas.restaurants.adapters.driven.connection.mapper.ISendMessageRequestMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter.EmployeeAdapter;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter.MealAdapter;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter.OrderAdapter;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter.RestaurantAdapter;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IEmployeeEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IMealEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IOrderEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IRestaurantEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IEmployeeRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IMealRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IOrderRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IRestaurantRepository;
import plazadecomidas.restaurants.domain.primaryport.IEmployeeServicePort;
import plazadecomidas.restaurants.domain.primaryport.IMealServicePort;
import plazadecomidas.restaurants.domain.primaryport.IOrderServicePort;
import plazadecomidas.restaurants.domain.primaryport.IRestaurantServicePort;
import plazadecomidas.restaurants.domain.primaryport.usecase.EmployeeUseCase;
import plazadecomidas.restaurants.domain.primaryport.usecase.MealUseCase;
import plazadecomidas.restaurants.domain.primaryport.usecase.OrderUseCase;
import plazadecomidas.restaurants.domain.primaryport.usecase.RestaurantUseCase;
import plazadecomidas.restaurants.domain.secondaryport.IEmployeePersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IMealPersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IOrderMessagingPort;
import plazadecomidas.restaurants.domain.secondaryport.IOrderPersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IRestaurantPersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IUserConnectionPort;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IRestaurantRepository restaurantRepository;
    private final IMealRepository mealRepository;
    private final IOrderRepository orderRepository;
    private final IEmployeeRepository employeeRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final IMealEntityMapper mealEntityMapper;
    private final IOrderEntityMapper orderEntityMapper;
    private final IEmployeeEntityMapper employeeEntityMapper;
    private final IUserFeignClient userFeignClient;

    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort() {
        return new RestaurantAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort(IRestaurantPersistencePort restaurantPersistencePort,
                                                         IUserConnectionPort userConnectionPort) {
        return new RestaurantUseCase(restaurantPersistencePort, userConnectionPort);
    }

    @Bean
    public IMealPersistencePort mealPersistencePort() {
        return new MealAdapter(mealRepository, mealEntityMapper);
    }

    @Bean
    public IMealServicePort mealServicePort(IMealPersistencePort mealPersistencePort, IRestaurantPersistencePort restaurantPersistencePort) {
        return new MealUseCase(mealPersistencePort, restaurantPersistencePort);
    }

    @Bean
    public IOrderPersistencePort orderPersistencePort(IEmployeePersistencePort employeePersistencePort) {
        return new OrderAdapter(orderRepository, mealRepository, orderEntityMapper, employeePersistencePort);
    }

    @Bean
    public IOrderServicePort orderServicePort(IOrderPersistencePort orderPersistencePort, IUserConnectionPort userConnectionPort, IOrderMessagingPort orderMessagingPort) {
        return new OrderUseCase(orderPersistencePort, userConnectionPort, orderMessagingPort);
    }

    @Bean
    public IEmployeePersistencePort employeePersistencePort() {
        return new EmployeeAdapter(employeeRepository, employeeEntityMapper);
    }

    @Bean
    public IEmployeeServicePort employeeServicePort(IEmployeePersistencePort employeePersistencePort, IRestaurantPersistencePort restaurantPersistencePort) {
        return new EmployeeUseCase(employeePersistencePort, restaurantPersistencePort);
    }

    @Bean
    public IOrderMessagingPort orderMessagingPort(ISendMessageRequestMapper sendMessageRequestMapper,
                                                  IMessagingFeignClient messagingFeignClient) {
        return new MessagingAdapter(sendMessageRequestMapper, messagingFeignClient);
    }

    @Bean
    public IUserConnectionPort userConnectionPort(IUserFeignClient userFeignClient) {
        return new UserAdapter(userFeignClient);
    }
}
