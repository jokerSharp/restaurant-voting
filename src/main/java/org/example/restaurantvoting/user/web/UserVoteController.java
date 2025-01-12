package org.example.restaurantvoting.user.web;

import lombok.extern.slf4j.Slf4j;
import org.example.restaurantvoting.app.AuthUser;
import org.example.restaurantvoting.common.exception.NotFoundException;
import org.example.restaurantvoting.restaurant.VotesUtil;
import org.example.restaurantvoting.restaurant.service.VoteService;
import org.example.restaurantvoting.restaurant.to.VoteTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = UserVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UserVoteController {

    static final String REST_URL = "/api/user/votes";

    @Autowired
    private VoteService voteService;

    @GetMapping("/current-day")
    public VoteTo getCurrentDayVote(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get the current day vote for a user with id={}", authUser.getUser().getId());
        return VotesUtil.createToFromVote(voteService.findCurrentDayVote(authUser.getUser())
                .orElseThrow(() -> new NotFoundException("Current day vote is not found")));
    }

    @GetMapping
    public List<VoteTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get vote history for a user with id={}", authUser.getUser().getId());
        return voteService.findAll(authUser.getUser()).stream()
                .map(VotesUtil::createToFromVote)
                .toList();
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@RequestBody VoteTo voteTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("vote for the restaurant id={} as the user with id={}", voteTo.getRestaurantId(), authUser.id());
        voteService.processVote(voteTo.getRestaurantId(), authUser.getUser());
    }
}
