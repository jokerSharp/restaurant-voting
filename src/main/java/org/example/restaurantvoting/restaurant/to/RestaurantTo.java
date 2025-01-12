package org.example.restaurantvoting.restaurant.to;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.example.restaurantvoting.common.to.NamedTo;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {

    List<DishTo> dishes;

    public RestaurantTo(Integer id, String name) {
        super(id, name);
        dishes = null;
    }

    public RestaurantTo(Integer id, String name, List<DishTo> dishes) {
        super(id, name);
        this.dishes = dishes;
    }
}
