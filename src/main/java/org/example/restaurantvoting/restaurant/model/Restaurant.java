package org.example.restaurantvoting.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.restaurantvoting.common.model.NamedEntity;
import org.example.restaurantvoting.user.model.User;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "restaurant")
    @Schema(hidden = true)
    private Set<Dish> dishes;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "restaurant_user",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "user_id"}, name = "uk_restaurant_user")})
    private List<User> users;
}
