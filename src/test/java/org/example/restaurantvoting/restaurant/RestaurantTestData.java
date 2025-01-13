package org.example.restaurantvoting.restaurant;

import org.example.restaurantvoting.MatcherFactory;
import org.example.restaurantvoting.restaurant.model.Restaurant;

public class RestaurantTestData {

    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "dishes", "votes");

    public static final int RESTAURANT_1_ID = 1;
    public static final String RESTAURANT_1_NAME = "Mirazur";
    public static final int RESTAURANT_2_ID = 2;
    public static final int NOT_FOUND = 100;

    public static final Restaurant mirazur = new Restaurant (RESTAURANT_1_ID, RESTAURANT_1_NAME, null, null);
    public static final Restaurant geranium = new Restaurant (RESTAURANT_2_ID, "Geranium", null, null);

    public static Restaurant getNew() {
        return new Restaurant(null, "New", null, null);
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT_1_ID, "UpdatedName", null, null);
    }
}
