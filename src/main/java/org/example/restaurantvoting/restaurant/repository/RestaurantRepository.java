package org.example.restaurantvoting.restaurant.repository;

import org.example.restaurantvoting.common.BaseRepository;
import org.example.restaurantvoting.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("select r from Restaurant r left join fetch r.dishes d")
    List<Restaurant> findWithDishes();
}
