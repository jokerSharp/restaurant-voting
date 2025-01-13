package org.example.restaurantvoting.restaurant.web;

import lombok.AllArgsConstructor;
import org.example.restaurantvoting.restaurant.model.Restaurant;
import org.example.restaurantvoting.restaurant.repository.RestaurantRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UniqueRestaurantNameValidator implements Validator {

    public static final String EXCEPTION_DUPLICATE_NAME = "Restaurant with this name already exists";

    private final RestaurantRepository repository;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Restaurant.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        Restaurant restaurant = (Restaurant) target;
        Optional<Restaurant> optional = repository.findByName(restaurant.getName());
        if (optional.isPresent()) {
            errors.rejectValue("name", "", EXCEPTION_DUPLICATE_NAME);
        }
    }
}
