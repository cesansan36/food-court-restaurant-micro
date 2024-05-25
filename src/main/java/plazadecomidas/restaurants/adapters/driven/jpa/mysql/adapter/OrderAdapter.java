package plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.OrderEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.OrderMealEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryMismatchException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.WrongInputException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IOrderEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IMealRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IOrderRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.util.PersistenceConstants;
import plazadecomidas.restaurants.domain.model.Order;
import plazadecomidas.restaurants.domain.secondaryport.IEmployeePersistencePort;
import plazadecomidas.restaurants.domain.secondaryport.IOrderPersistencePort;
import plazadecomidas.restaurants.domain.util.DomainConstants;

import java.util.List;

@RequiredArgsConstructor
public class OrderAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IMealRepository mealRepository;
    private final IOrderEntityMapper orderEntityMapper;
    private final IEmployeePersistencePort employeePersistencePort;

    @Override
    public int getAmountOfUnfinishedOrders(Long clientId) {
        return orderRepository.countPendingOrPreparingOrReadyOrdersByClientId(clientId);
    }

    @Override
    public void saveOrder(Order order) {

        OrderEntity orderEntity = orderEntityMapper.orderToOrderEntity(order);
        List<Long> mealIds = orderEntity.getMeals().stream().map(x -> x.getMeal().getId()).toList();
        List<Long> restaurantIds = mealRepository.findRestaurantIdsByMealIds(mealIds);

        for (Long restaurantId : restaurantIds) {
            if (!restaurantId.equals(orderEntity.getRestaurant().getId())) {
                throw new RegistryMismatchException(PersistenceConstants.MEAL_RESTAURANT_MISMATCH_EXCEPTION);
            }
        }

        for (OrderMealEntity orderMealEntity : orderEntity.getMeals()) {
            orderMealEntity.setOrder(orderEntity);
        }

        orderRepository.save(orderEntity);
    }

    @Override
    public List<Order> getOrdersByStatus(Long userId, Integer page, Integer size, String status) {

        Long restaurantId = employeePersistencePort.getRestaurantIdByEmployeeId(userId);

        List<OrderEntity> orderEntities;

        Pageable pagination = Pageable.ofSize(size).withPage(page);

        if (DomainConstants.OrderStatus.isValidStatus(status)) {
            orderEntities = orderRepository.findAllByRestaurantIdAndStatus(pagination, restaurantId, status).getContent();
        }
        else if (status.equals(PersistenceConstants.ALL_STATUS_FILTER)){
            orderEntities = orderRepository.findAllByRestaurantId(pagination, restaurantId).getContent();
        }
        else {
            throw new WrongInputException(PersistenceConstants.WRONG_STATUS_FILTER_MESSAGE);
        }

        return orderEntityMapper.orderEntitiesToOrders(orderEntities);
    }
}
