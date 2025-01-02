package org.example.restaurantvoting.restaurant.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.restaurantvoting.restaurant.model.Restaurant;
import org.example.restaurantvoting.restaurant.model.Vote;
import org.example.restaurantvoting.restaurant.repository.VoteRepository;
import org.example.restaurantvoting.user.model.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Slf4j
public class VoteService {

    private final VoteRepository repository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void inactivateVote() {
        log.info("delete votes by schedule");
        repository.deleteAll();
    }

    public Optional<Vote> findCurrentDayVote(User user) {
        return repository.findByUserAndCreatedAt(user, LocalDate.now());
    }

    @Transactional
    public Vote save(Restaurant restaurant, User user) {
        Vote newVote = new Vote(restaurant, user);
        return repository.save(newVote);
    }

    @Transactional
    public void reVote(int restaurantId, int userId) {
        repository.reVote(restaurantId, userId);
    }
}
