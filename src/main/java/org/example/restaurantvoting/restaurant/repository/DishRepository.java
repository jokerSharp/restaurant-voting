package org.example.restaurantvoting.restaurant.repository;

import org.example.restaurantvoting.common.BaseRepository;
import org.example.restaurantvoting.restaurant.model.Dish;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DishRepository extends BaseRepository<Dish> {

    List<Dish> findAllByRestaurantId(int restaurantId);

    @Query("select d from Dish d where d.restaurant.id=:restaurantId and d.id=:dishId")
    Dish findOneByRestaurantId(@Param("restaurantId") int restaurantId, @Param("dishId")int dishId);
}
