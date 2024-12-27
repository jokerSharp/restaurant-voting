package org.example.restaurantvoting.restaurant.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.restaurantvoting.user.model.User;

import java.time.LocalDate;

@Entity
@Table(name = "restaurant_user",
        uniqueConstraints =
                {@UniqueConstraint(columnNames = {"user_id", "created_at"}, name = "uk_user_date")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote {

    @EmbeddedId
    private VoteId voteId;

    @ManyToOne
    @MapsId("restaurantId")
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.DATE)
    private LocalDate createdAt;

    private boolean isDeleted = false;

    public Vote(VoteId voteId, Restaurant restaurant, User user) {
        this.voteId = voteId;
        this.restaurant = restaurant;
        this.user = user;
        this.createdAt = LocalDate.now();
    }
}
