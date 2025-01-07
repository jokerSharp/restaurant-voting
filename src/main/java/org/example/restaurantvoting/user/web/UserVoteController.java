package org.example.restaurantvoting.user.web;

import lombok.extern.slf4j.Slf4j;
import org.example.restaurantvoting.app.AuthUser;
import org.example.restaurantvoting.common.exception.AppException;
import org.example.restaurantvoting.common.exception.NotFoundException;
import org.example.restaurantvoting.restaurant.model.Restaurant;
import org.example.restaurantvoting.user.model.Vote;
import org.example.restaurantvoting.restaurant.repository.RestaurantRepository;
import org.example.restaurantvoting.user.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.example.restaurantvoting.common.exception.ErrorType.BAD_DATA;

@RestController
@RequestMapping(value = UserVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UserVoteController {

    static final String REST_URL = "/api/user/vote";

    private final static LocalTime VOTE_END_TIME = LocalTime.of(11, 0);

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private VoteService voteService;

    @Autowired
    private Clock clock;

    @GetMapping
    public Integer get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get the current day vote for a user with id={}", authUser.getUser().getId());
        Optional<Vote> currentDayVote = voteService.findCurrentDayVote(authUser.getUser());
        if (currentDayVote.isPresent()) {
            return currentDayVote.get().getRestaurant().getId();
        } else {
            throw new NotFoundException("Current day vote not found");
        }
    }

    @PostMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@PathVariable int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("vote for the restaurant id={} as the user with id={}", restaurantId, authUser.id());
        Optional<Vote> currentDayVote = voteService.findCurrentDayVote(authUser.getUser());
        if (currentDayVote.isEmpty()) {
            Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
            voteService.save(restaurant, authUser.getUser());
        } else if (LocalTime.ofInstant(clock.instant(), ZoneId.systemDefault()).isBefore(VOTE_END_TIME)) {
            voteService.reVote(restaurantId, authUser.id());
        } else {
            throw new AppException("You have already made your choice for today", BAD_DATA);
        }
    }
}
