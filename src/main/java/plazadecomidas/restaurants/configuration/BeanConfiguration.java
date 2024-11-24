package plazadecomidas.restaurants.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import plazadecomidas.restaurants.adapters.driven.connection.adapter.MessagingAdapter;
import plazadecomidas.restaurants.adapters.driven.connection.adapter.TracingConnectionAdapter;
import plazadecomidas.restaurants.adapters.driven.connection.adapter.UserConnectionAdapter;
import plazadecomidas.restaurants.adapters.driven.connection.feign.IMessagingFeignClient;
import plazadecomidas.restaurants.adapters.driven.connection.feign.ITracingFeignClient;
import plazadecomidas.restaurants.adapters.driven.connection.feign.IUserFeignClient;
import plazadecomidas.restaurants.adapters.driven.connection.mapper.IAddRecordRequestFeignMapper;
import plazadecomidas.restaurants.adapters.driven.connection.mapper.IRecordResponseFeignMapper;
import plazadecomidas.restaurants.adapters.driven.connection.mapper.ISendMessageRequestMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter.CategoryAdapter;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter.EmployeeAdapter;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter.MealAdapter;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter.OrderAdapter;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter.RestaurantAdapter;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IEmployeeEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IMealEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IOrderEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IRestaurantEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.ICategoryRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IEmployeeRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IMealRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IOrderRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IRestaurantRepository;
import plazadecomidas.restaurants.domain.primaryport.IEmployeeServicePort;
import plazadecomidas.restaurants.domain.primaryport.IMealServicePort;
import plazadecomidas.restaurants.domain.primaryport.IOrderRecordPrimaryPort;
import plazadecomidas.restaurants.domain.primaryport.IOrderServicePort;
import plazadecomidas.restaurants.domain.primaryport.IRestaurantServicePort;
import plazadecomidas.restaurants.domain.primaryport.usecase.EmployeeUseCase;
import plazadecomidas.restaurants.domain.primaryport.usecase.MealUseCase;
import plazadecomidas.restaurants.domain.primaryport.usecase.OrderRecordUseCase;
import plazadecomidas.restaurants.domain.primaryport.usecase.OrderUseCase;
import plazadecomidas.restaurants.domain.primaryport.usecase.RestaurantUseCase;
import plazadecomidas.restaurants.domain.secondaryport.ICategoryPersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IEmployeePersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IMealPersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IOrderMessagingPort;
import plazadecomidas.restaurants.domain.secondaryport.IOrderPersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IRestaurantPersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.ITracingConnectionPort;
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
    public IMealServicePort mealServicePort(IMealPersistencePort mealPersistencePort, ICategoryPersistencePort categoryPersistencePort, IRestaurantPersistencePort restaurantPersistencePort) {
        return new MealUseCase(mealPersistencePort, categoryPersistencePort, restaurantPersistencePort);
    }

    @Bean
    public IOrderPersistencePort orderPersistencePort(IEmployeePersistencePort employeePersistencePort) {
        return new OrderAdapter(orderRepository, mealRepository, orderEntityMapper, employeePersistencePort);
    }

    @Bean
    public IOrderServicePort orderServicePort(IOrderPersistencePort orderPersistencePort,
                                              IUserConnectionPort userConnectionPort,
                                              IOrderMessagingPort orderMessagingPort,
                                              IOrderRecordPrimaryPort orderRecordPrimaryPort) {
        return new OrderUseCase(orderPersistencePort, userConnectionPort, orderMessagingPort, orderRecordPrimaryPort);
    }

    @Bean
    public ICategoryPersistencePort categoryPersistencePort(ICategoryRepository categoryRepository) {
        return new CategoryAdapter(categoryRepository);
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
        return new UserConnectionAdapter(userFeignClient);
    }

    @Bean
    public IOrderRecordPrimaryPort orderRecordPrimaryPort(ITracingConnectionPort tracingConnectionPort, IUserConnectionPort userConnectionPort) {
        return new OrderRecordUseCase(tracingConnectionPort, userConnectionPort);
    }

    @Bean
    public ITracingConnectionPort tracingConnectionPort(ITracingFeignClient tracingFeignClient, IRecordResponseFeignMapper recordResponseFeignMapper, IAddRecordRequestFeignMapper recordRequestFeignMapper) {
        return new TracingConnectionAdapter(tracingFeignClient, recordResponseFeignMapper, recordRequestFeignMapper);
    }
}
