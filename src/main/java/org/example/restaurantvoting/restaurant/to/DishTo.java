package org.example.restaurantvoting.restaurant.to;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.example.restaurantvoting.common.to.NamedTo;
import org.example.restaurantvoting.common.validation.View;
import org.example.restaurantvoting.restaurant.model.Restaurant;

import java.math.BigDecimal;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends NamedTo {

    BigDecimal price;

    @Schema(hidden = true)
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @NotNull(groups = View.Persist.class)
    Restaurant restaurant;

    public DishTo(Integer id, String name, BigDecimal price, Restaurant restaurant) {
        super(id, name);
        this.price = price;
        this.restaurant = restaurant;
    }
}
