package org.example.restaurantvoting.restaurant;

import lombok.experimental.UtilityClass;
import org.example.restaurantvoting.restaurant.model.Dish;
import org.example.restaurantvoting.restaurant.to.DishTo;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class DishesUtil {

    public static Dish createDishFromTo(DishTo dishTo) {
        return new Dish(dishTo.getId(), dishTo.getName(), getDishPrice(dishTo.getPrice()), dishTo.getActualityDate());
    }

    public static DishTo createToFromDish(Dish dish) {
        return new DishTo(dish.getId(), dish.getName(), getDishToPrice(dish.getPrice()), dish.getActualityDate());
    }

    private static long getDishPrice(BigDecimal decimalPrice) {
        return decimalPrice.multiply(BigDecimal.valueOf(100)).longValue();
    }

    private static BigDecimal getDishToPrice(long longPrice) {
        return BigDecimal.valueOf(longPrice)
                .divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
    }
}
