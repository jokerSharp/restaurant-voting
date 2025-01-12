package org.example.restaurantvoting.restaurant.service;

import lombok.AllArgsConstructor;
import org.example.restaurantvoting.common.exception.AppException;
import org.example.restaurantvoting.restaurant.model.Restaurant;
import org.example.restaurantvoting.restaurant.model.Vote;
import org.example.restaurantvoting.restaurant.repository.RestaurantRepository;
import org.example.restaurantvoting.restaurant.repository.VoteRepository;
import org.example.restaurantvoting.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.example.restaurantvoting.common.exception.ErrorType.BAD_DATA;

@Service
@AllArgsConstructor
public class VoteService {

    private static final LocalTime VOTE_END_TIME = LocalTime.of(21, 0);

    private final VoteRepository voteRepository;

    private final RestaurantRepository restaurantRepository;

    private final Clock clock;

    public Optional<Vote> findCurrentDayVote(User user) {
        return voteRepository.findByUserAndCreatedAt(user, LocalDate.now());
    }

    public List<Vote> findAll(User user) {
        return voteRepository.findByUser(user);
    }

    public Vote save(Restaurant restaurant, User user) {
        Vote newVote = new Vote(restaurant, user);
        return voteRepository.save(newVote);
    }

    public void reVote(int restaurantId, int userId) {
        voteRepository.reVote(restaurantId, userId);
    }

    @Transactional
    public void processVote(int restaurantId, User user) {
        //todo check date of the vote
        Optional<Vote> currentDayVote = findCurrentDayVote(user);
        if (currentDayVote.isEmpty()) {
            Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
            save(restaurant, user);
        } else if (LocalTime.now(clock).isBefore(VOTE_END_TIME)) {
            reVote(restaurantId, user.getId());
        } else {
            throw new AppException("You have already made your choice for today", BAD_DATA);
        }
    }
}
