package org.example.restaurantvoting.restaurant.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.restaurantvoting.app.AuthUser;
import org.example.restaurantvoting.restaurant.RestaurantsUtil;
import org.example.restaurantvoting.restaurant.model.Restaurant;
import org.example.restaurantvoting.restaurant.repository.RestaurantRepository;
import org.example.restaurantvoting.restaurant.to.RestaurantTo;
import org.example.restaurantvoting.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalTime;
import java.util.List;

import static org.example.restaurantvoting.common.validation.ValidationUtil.assureIdConsistent;
import static org.example.restaurantvoting.common.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantController {

    static final String REST_URL = "/api/restaurants";

    @Autowired
    private RestaurantRepository repository;

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get restaurant with id={}", id);
        return repository.getExisted(id);
    }

    @GetMapping
    public List<RestaurantTo> getAll() {
        log.info("get all restaurants");
        return repository.findAll().stream().map(RestaurantsUtil::createToFromRestaurant).toList();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        repository.save(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }

    @PatchMapping("/{id}/vote")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@PathVariable int id, @AuthenticationPrincipal AuthUser authUser) {
        log.info("vote for the restaurant id={} as the user with id={}", id, authUser.id());
        Restaurant restaurant = repository.getExisted(id);
        List<User> users = restaurant.getUsers();
        if (!users.contains(authUser.getUser())) {
            restaurant.getUsers().add(authUser.getUser());
            repository.save(restaurant);
        } else if (LocalTime.now().isBefore(LocalTime.of(11, 0))) {
            restaurant.getUsers().add(authUser.getUser());
            repository.save(restaurant);
        }
    }
}
