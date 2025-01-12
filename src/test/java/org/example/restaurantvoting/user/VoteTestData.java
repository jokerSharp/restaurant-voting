package org.example.restaurantvoting.user;

import org.example.restaurantvoting.MatcherFactory;
import org.example.restaurantvoting.restaurant.to.VoteTo;

import java.time.LocalDate;
import java.time.Month;

public class VoteTestData {

    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class);

    public static final VoteTo vote1 = new VoteTo(1, 1, LocalDate.of(2025, Month.JANUARY, 10));
    public static final VoteTo vote2 = new VoteTo(2, 2, LocalDate.now());

}
