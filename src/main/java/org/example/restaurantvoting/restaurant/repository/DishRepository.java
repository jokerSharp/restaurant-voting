package org.example.restaurantvoting.restaurant.repository;

import org.example.restaurantvoting.common.BaseRepository;
import org.example.restaurantvoting.restaurant.model.Dish;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    List<Dish> findAllByRestaurantId(int restaurantId);

    @Query("select d from Dish d where d.restaurant.id=:restaurantId and d.id=:dishId")
    Optional<Dish> findOneByRestaurantId(@Param("restaurantId") int restaurantId, @Param("dishId")int dishId);
}
