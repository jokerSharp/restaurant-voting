package org.example.restaurantvoting.restaurant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.restaurantvoting.common.model.BaseEntity;
import org.example.restaurantvoting.user.model.User;

import java.time.LocalDate;

@Entity
@Table(name = "vote",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "created_at"}, name = "uk_user_date")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"restaurant", "user"})
public class Vote extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @NotNull
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @NotNull
    private LocalDate createdAt;

    public Vote(Restaurant restaurant, User user) {
        this.restaurant = restaurant;
        this.user = user;
        this.createdAt = LocalDate.now();
    }
}
