package org.example.restaurantvoting.restaurant.web;

import org.example.restaurantvoting.AbstractControllerTest;
import org.example.restaurantvoting.common.util.JsonUtil;
import org.example.restaurantvoting.restaurant.model.Restaurant;
import org.example.restaurantvoting.restaurant.repository.RestaurantRepository;
import org.example.restaurantvoting.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.example.restaurantvoting.restaurant.RestaurantTestData.*;
import static org.example.restaurantvoting.restaurant.web.RestaurantController.REST_URL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    private static final String REST_URL_RESTAURANT_1_ID = REST_URL_SLASH + RESTAURANT_1_ID;

    @Autowired
    private RestaurantRepository repository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_RESTAURANT_1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(mirazur));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(mirazur, geranium));
    }

    @Test
    void getAllUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isCreated());

        Restaurant created = RESTAURANT_MATCHER.readFromJson(action);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(repository.getExisted(newId), newRestaurant);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createInvalid() throws Exception {
        Restaurant newRestaurant = new Restaurant(null, null, null, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_RESTAURANT_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(repository.getExisted(RESTAURANT_1_ID), updated);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Restaurant updated = getUpdated();
        updated.setName("");
        perform(MockMvcRequestBuilders.put(REST_URL_RESTAURANT_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_RESTAURANT_1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(RESTAURANT_1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}