package org.example.restaurantvoting.restaurant;

import lombok.experimental.UtilityClass;
import org.example.restaurantvoting.restaurant.model.Restaurant;
import org.example.restaurantvoting.restaurant.to.RestaurantTo;

@UtilityClass
public class RestaurantsUtil {

    public static RestaurantTo createToFromRestaurant(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getVotes());
    }
}
