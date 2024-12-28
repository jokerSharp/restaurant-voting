package org.example.restaurantvoting.restaurant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.restaurantvoting.common.model.BaseEntity;
import org.example.restaurantvoting.user.model.User;

import java.time.LocalDate;

@Entity
@Table(name = "restaurant_user",
        uniqueConstraints =
                {@UniqueConstraint(columnNames = {"user_id", "created_at"}, name = "uk_user_date")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

    @ManyToOne()
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Temporal(TemporalType.DATE)
    private LocalDate createdAt;

    public Vote(Restaurant restaurant, User user) {
        this.restaurant = restaurant;
        this.user = user;
        this.createdAt = LocalDate.now();
    }
}
