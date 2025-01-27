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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = UserVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UserVoteController {

    static final String REST_URL = "/api/user/votes";
    static final String BY_DATE_URL = "/by-date";

    @Autowired
    private VoteService voteService;

    @GetMapping(BY_DATE_URL)
    public VoteTo get(@AuthenticationPrincipal AuthUser authUser,
                      @RequestParam(value = "date", required = false) LocalDate date) {
        if (date == null) date = LocalDate.now();
        log.info("get the vote for the user with id={} for the date={}", authUser.getUser().getId(), date);
        return VotesUtil.createToFromVote(voteService.findVoteByDate(authUser.getUser(), date)
                .orElseThrow(() -> new NotFoundException("Vote for that date is not found")));
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
        voteService.processVote(voteTo, authUser.getUser());
    }
}
