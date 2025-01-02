package org.example.restaurantvoting.restaurant.repository;

import org.example.restaurantvoting.common.BaseRepository;
import org.example.restaurantvoting.restaurant.model.Vote;
import org.example.restaurantvoting.user.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    Optional<Vote> findByUserAndCreatedAt(User user, LocalDate createdAt);

    @Transactional
    @Modifying
    @Query("update Vote v set v.restaurant.id=:restaurantId where v.user.id=:userId")
    void reVote(@Param("restaurantId") int restaurantId, @Param("userId") int userId);
}
