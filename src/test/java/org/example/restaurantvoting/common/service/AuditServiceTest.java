package org.example.restaurantvoting.common.service;

import org.example.restaurantvoting.common.exception.AppException;
import org.example.restaurantvoting.restaurant.DishTestData;
import org.example.restaurantvoting.restaurant.RestaurantTestData;
import org.example.restaurantvoting.restaurant.model.Dish;
import org.example.restaurantvoting.restaurant.model.Restaurant;
import org.example.restaurantvoting.restaurant.repository.DishRepository;
import org.example.restaurantvoting.restaurant.repository.RestaurantRepository;
import org.example.restaurantvoting.user.UserTestData;
import org.example.restaurantvoting.user.model.User;
import org.example.restaurantvoting.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AuditServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AuditService auditService;

    @Test
    void getPreviousUserVersion() {
        User user = UserTestData.getNew();
        user = userRepository.save(user);
        user.setName("updated user");
        userRepository.save(user);
        User audited = auditService.getPreviousEntityVersion(user.getId(), User.class);

        assertEquals(user, audited);
    }

    @Test
    void getPreviousRestaurantVersion() {
        Restaurant restaurant = RestaurantTestData.getNew();
        restaurant = restaurantRepository.save(restaurant);
        restaurant.setName("updated restaurant");
        restaurantRepository.save(restaurant);
        Restaurant audited = auditService.getPreviousEntityVersion(restaurant.getId(), Restaurant.class);

        assertEquals(restaurant, audited);

    }

    @Test
    void getPreviousDishVersion() {
        Dish dish = DishTestData.getNew();
        Restaurant restaurant = RestaurantTestData.getNew();
        restaurantRepository.save(restaurant);
        dish.setRestaurant(restaurant);
        dish = dishRepository.save(dish);
        dish.setName("updated dish");
        dishRepository.save(dish);
        Dish audited = auditService.getPreviousEntityVersion(dish.getId(), Dish.class);

        assertEquals(dish, audited);

    }

    @Test
    void getPreviousEntityVersionNotFound() {
        Assertions.assertThrows(AppException.class,
                () -> auditService.getPreviousEntityVersion(UserTestData.NOT_FOUND, User.class));

    }
}