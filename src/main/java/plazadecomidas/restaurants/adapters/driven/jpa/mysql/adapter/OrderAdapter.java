package plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter;

import lombok.RequiredArgsConstructor;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.OrderEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.OrderMealEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryMismatchException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IOrderEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IMealRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IOrderRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.util.PersistenceConstants;
import plazadecomidas.restaurants.domain.model.Order;
import plazadecomidas.restaurants.domain.secondaryport.IOrderPersistencePort;

import java.util.List;

@RequiredArgsConstructor
public class OrderAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IMealRepository mealRepository;
    private final IOrderEntityMapper orderEntityMapper;

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
}
