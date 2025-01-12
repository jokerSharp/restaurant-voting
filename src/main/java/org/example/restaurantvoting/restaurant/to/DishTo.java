package org.example.restaurantvoting.restaurant.to;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.example.restaurantvoting.common.to.NamedTo;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends NamedTo {

    @NotNull
    BigDecimal price;

    @NotNull
    LocalDate actualityDate;

    public DishTo(Integer id, String name, BigDecimal price, LocalDate actualityDate) {
        super(id, name);
        this.price = price;
        this.actualityDate = actualityDate;
    }
}
