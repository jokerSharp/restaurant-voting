package org.example.restaurantvoting.restaurant;

import lombok.experimental.UtilityClass;
import org.example.restaurantvoting.restaurant.model.Vote;
import org.example.restaurantvoting.restaurant.to.VoteTo;

@UtilityClass
public class VotesUtil {

    public static VoteTo createToFromVote(Vote vote) {
        return new VoteTo(vote.getId(), vote.getRestaurant().getId(), vote.getCreatedAt());
    }
}
