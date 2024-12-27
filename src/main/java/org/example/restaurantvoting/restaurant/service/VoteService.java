package org.example.restaurantvoting.restaurant.service;

import lombok.AllArgsConstructor;
import org.example.restaurantvoting.restaurant.model.Restaurant;
import org.example.restaurantvoting.restaurant.model.Vote;
import org.example.restaurantvoting.restaurant.model.VoteId;
import org.example.restaurantvoting.restaurant.repository.VoteRepository;
import org.example.restaurantvoting.user.model.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class VoteService {

    private final VoteRepository repository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void inactivateVote() {
        List<Vote> votes = repository.findAll();
        for (Vote vote : votes) {
            vote.setDeleted(true);
            repository.save(vote);
        }
    }

    public Optional<Vote> findCurrentDayVote(User user) {
        return repository.findByUserAndCreatedAt(user, LocalDate.now());
    }

    @Transactional
    public Vote save(Restaurant restaurant, User user) {
        VoteId voteId = new VoteId(restaurant.getId(), user.getId());
        Vote newVote = new Vote(voteId, restaurant, user);
        return repository.save(newVote);
    }

    @Transactional
    public void reVote(int restaurantId, int userId) {
        repository.updateVote(restaurantId, userId);
    }
}
