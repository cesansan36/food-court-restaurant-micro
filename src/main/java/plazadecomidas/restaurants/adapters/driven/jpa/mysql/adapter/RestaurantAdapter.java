package plazadecomidas.restaurants.adapters.driven.jpa.mysql.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryAlreadyExistsException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.exception.RegistryNotFoundException;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.mapper.IRestaurantEntityMapper;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.repository.IRestaurantRepository;
import plazadecomidas.restaurants.adapters.driven.jpa.mysql.util.PersistenceConstants;
import plazadecomidas.restaurants.domain.model.Restaurant;
import plazadecomidas.restaurants.domain.secondaryport.IRestaurantPersistencePort;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RestaurantAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        Optional<RestaurantEntity> previousRestaurant = restaurantRepository.findByAddress(restaurant.getAddress());

        if (previousRestaurant.isPresent()) {
            throw new RegistryAlreadyExistsException(PersistenceConstants.RESTAURANT_ALREADY_EXISTS_MESSAGE);
        }

        restaurantRepository.save(restaurantEntityMapper.toEntity(restaurant));
    }

    @Override
    public boolean existsRestaurantOwnerPair(Long idRestaurant, Long idOwner) {
        Optional<RestaurantEntity> restaurant = restaurantRepository.findByIdAndOwnerId(idRestaurant, idOwner);

        if (restaurant.isEmpty()) {
            throw new RegistryNotFoundException(PersistenceConstants.RESTAURANT_OWNER_MATCH_NOT_FOUND);
        }

        return true;
    }

    @Override
    public List<Restaurant> getAllRestaurants(Integer page, Integer size) {

        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        Pageable pagination = PageRequest.of(page, size, sort);

        List<RestaurantEntity> restaurants = restaurantRepository.findAll(pagination).getContent();
        return restaurantEntityMapper.toDomainList(restaurants);
    }

    @Override
    public boolean existsOwnerRestaurantMatch(Long idOwner, Long idRestaurant) {
        return restaurantRepository.existsByOwnerIdAndId(idOwner, idRestaurant);
    }
}
