package plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter;

import lombok.RequiredArgsConstructor;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.MealEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.OrderMealEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.OrderEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IOrderEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IMealRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IOrderRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IRestaurantRepository;
import plazadecomidas.restaurants.domain.model.Order;
import plazadecomidas.restaurants.domain.secondaryport.IOrderPersistencePort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OrderAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IMealRepository mealRepository;
    private final IRestaurantRepository restaurantRepository;
    private final IOrderEntityMapper orderEntityMapper;

    @Override
    public void saveOrder(Order order) {
//        orderRepository.save(orderEntityMapper.orderToOrderEntity(order));

        Optional<RestaurantEntity> ore = restaurantRepository.findById(2L);
        if (ore.isEmpty()) {
            System.out.println("not found");
            return;
        }
        RestaurantEntity re = ore.get();

        Optional<MealEntity> ome = mealRepository.findByNameAndRestaurant_Id("chocolate", 2L);
        if (ome.isEmpty()) {
            System.out.println("not found");
            return;
        }
        MealEntity me = ome.get();

        OrderEntity oe = new OrderEntity();
        oe.setDate(LocalDate.now());
        oe.setStatus("PENDING");
        oe.setIdClient(1L);
        oe.setIdChef(1L);
        oe.setRestaurant(re);

        OrderMealEntity moe = new OrderMealEntity(me, oe, 3);
        oe.getMeals().add(moe);

        orderRepository.save(oe);
    }
}
