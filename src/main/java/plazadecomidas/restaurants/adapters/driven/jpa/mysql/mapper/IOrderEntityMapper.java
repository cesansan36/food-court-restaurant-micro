package plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.MealEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.OrderEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.OrderMealEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import plazadecomidas.restaurants.domain.model.MealOrder;
import plazadecomidas.restaurants.domain.model.Order;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderEntityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurant", source = "idRestaurant", qualifiedByName = "mapRestaurant")
    @Mapping(target = "meals", source = "meals", qualifiedByName = "mapMeals")
    OrderEntity orderToOrderEntity(Order order);

    @Named("mapMeals")
    default List<OrderMealEntity> mapMeals(List<MealOrder> meals) {
        List<OrderMealEntity> orderMeals = new ArrayList<>();

        for (MealOrder meal : meals) {
            OrderMealEntity orderMealEntity = new OrderMealEntity();

            MealEntity mealEntity = new MealEntity();
            mealEntity.setId(meal.getIdMeal());
            orderMealEntity.setMeal(mealEntity);

            orderMealEntity.setAmount(meal.getQuantity());

            orderMeals.add(orderMealEntity);
        }

        return orderMeals;
    }

    @Named("mapRestaurant")
    default RestaurantEntity mapRestaurant(Long idRestaurant) {
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(idRestaurant);
        return restaurantEntity;
    }
}
