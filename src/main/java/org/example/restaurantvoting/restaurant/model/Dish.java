package org.example.restaurantvoting.restaurant.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.example.restaurantvoting.common.model.NamedEntity;
import org.example.restaurantvoting.common.validation.View;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "dish")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"restaurant"})
public class Dish extends NamedEntity {

    @PositiveOrZero
    private long price;

    @Schema(hidden = true)
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @NotNull(groups = View.Persist.class)
    private Restaurant restaurant;

    public Dish(Integer id, String name, long price) {
        super(id, name);
        this.price = price;
    }

    public Dish(Integer id, String name, long price, Restaurant restaurant) {
        super(id, name);
        this.price = price;
        this.restaurant = restaurant;
    }
}
