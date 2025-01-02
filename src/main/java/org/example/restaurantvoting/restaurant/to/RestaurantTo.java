package org.example.restaurantvoting.restaurant.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.example.restaurantvoting.common.to.NamedTo;
import org.example.restaurantvoting.restaurant.model.Vote;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {

    int votes;

    public RestaurantTo(Integer id, String name, List<Vote> votes) {
        super(id, name);
        this.votes = votes.size();
    }
}
