package org.example.restaurantvoting.restaurant.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.restaurantvoting.restaurant.model.Dish;
import org.example.restaurantvoting.restaurant.repository.DishRepository;
import org.example.restaurantvoting.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.example.restaurantvoting.common.validation.ValidationUtil.assureIdConsistent;
import static org.example.restaurantvoting.common.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class DishController {

    static final String REST_URL = "/api/restaurants/{restaurantId}/dishes";

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private DishRepository repository;

    @GetMapping("/{id}")
    public Dish get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get dish with id={}", id);
        return repository.findOneByRestaurantId(restaurantId, id);
    }

    @GetMapping
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("get all dishes for the restaurant id={}", restaurantId);
        return repository.findAllByRestaurantId(restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@PathVariable int restaurantId, @Valid @RequestBody Dish dish) {
        log.info("create {}", dish);
        checkNew(dish);
        dish.setRestaurant(restaurantRepository.getExisted(restaurantId));
        Dish created = repository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@PathVariable int restaurantId, @Valid @RequestBody Dish dish, @PathVariable int id) {
        log.info("update {} with id={}", dish, id);
        assureIdConsistent(dish, id);
        repository.save(dish);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }
}
