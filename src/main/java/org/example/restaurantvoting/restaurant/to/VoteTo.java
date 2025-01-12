package org.example.restaurantvoting.restaurant.to;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.example.restaurantvoting.common.to.BaseTo;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {

    int restaurantId;

    @NotNull
    LocalDate voteDate;

    public VoteTo(Integer id, int restaurantId, LocalDate voteDate) {
        super(id);
        this.restaurantId = restaurantId;
        this.voteDate = voteDate;
    }
}
