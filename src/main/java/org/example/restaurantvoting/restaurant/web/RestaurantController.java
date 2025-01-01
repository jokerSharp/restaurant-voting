package org.example.restaurantvoting.restaurant.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.restaurantvoting.app.AuthUser;
import org.example.restaurantvoting.restaurant.RestaurantsUtil;
import org.example.restaurantvoting.restaurant.model.Restaurant;
import org.example.restaurantvoting.restaurant.model.Vote;
import org.example.restaurantvoting.restaurant.repository.RestaurantRepository;
import org.example.restaurantvoting.common.service.AuditService;
import org.example.restaurantvoting.restaurant.service.VoteService;
import org.example.restaurantvoting.restaurant.to.RestaurantTo;
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
import java.util.Optional;

import static org.example.restaurantvoting.common.validation.ValidationUtil.assureIdConsistent;
import static org.example.restaurantvoting.common.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantController {

    static final String REST_URL = "/api/restaurants";

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private VoteService voteService;

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get restaurant with id={}", id);
        return restaurantRepository.getExisted(id);
    }

    @GetMapping
    public List<RestaurantTo> getAll() {
        log.info("get all restaurants");
        return restaurantRepository.findAll().stream()
                .map(RestaurantsUtil::createToFromRestaurant)
                .toList();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        restaurantRepository.save(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        restaurantRepository.deleteExisted(id);
    }

    @PatchMapping("/{id}/vote")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@PathVariable int id, @AuthenticationPrincipal AuthUser authUser) {
        log.info("vote for the restaurant id={} as the user with id={}", id, authUser.id());
        Optional<Vote> currentDayVote = voteService.findCurrentDayVote(authUser.getUser());
        if (currentDayVote.isEmpty()) {
            Restaurant restaurant = restaurantRepository.getExisted(id);
            voteService.save(restaurant, authUser.getUser());
        } else if (LocalTime.now().isBefore(LocalTime.of(11, 0))) {
            voteService.reVote(id, authUser.id());
        }
    }

    @GetMapping("/{id}/previous-version")
    public Restaurant getPreviousVersion(@PathVariable int id) {
        return auditService.getPreviousEntityVersion(id, Restaurant.class);
    }
}
