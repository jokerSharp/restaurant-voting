package org.example.restaurantvoting.restaurant.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteId implements Serializable {

    private int restaurantId;

    private int userId;
}
