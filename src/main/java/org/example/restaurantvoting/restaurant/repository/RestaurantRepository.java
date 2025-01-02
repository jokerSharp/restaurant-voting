package org.example.restaurantvoting.restaurant.repository;

import org.example.restaurantvoting.common.BaseRepository;
import org.example.restaurantvoting.restaurant.model.Restaurant;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
}
