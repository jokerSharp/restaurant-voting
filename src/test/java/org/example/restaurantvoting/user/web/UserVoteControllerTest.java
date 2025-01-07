package org.example.restaurantvoting.user.web;

import org.example.restaurantvoting.AbstractControllerTest;
import org.example.restaurantvoting.MatcherFactory;
import org.example.restaurantvoting.user.repository.VoteRepository;
import org.example.restaurantvoting.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Clock;
import java.time.Instant;

import static org.example.restaurantvoting.restaurant.RestaurantTestData.*;
import static org.example.restaurantvoting.user.web.UserVoteController.REST_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserVoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + "/";

    private static final String REST_URL_RESTAURANT_1_ID_VOTE = REST_URL_SLASH + "1";

    @Autowired
    private VoteRepository voteRepository;

    @MockitoBean
    private Clock clock;


    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(MatcherFactory.usingEqualsComparator(Integer.class).contentJson(RESTAURANT_2_ID));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void vote() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_RESTAURANT_1_ID_VOTE))
                .andExpect(status().isNoContent());
        assertEquals(RESTAURANT_1_ID, voteRepository.getExisted(2).getRestaurant().getId());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void reVotePositive() throws Exception {
        //instant is parsed from UTC string so 07 in UTC will be 10 in UTC+3
        when(clock.instant()).thenReturn(Instant.parse("2025-01-07T07:00:00Z"));

        perform(MockMvcRequestBuilders.post(REST_URL_RESTAURANT_1_ID_VOTE))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void reVoteNegative() throws Exception {
        //instant is parsed from UTC string so 08 in UTC will be 11 in UTC+3
        when(clock.instant()).thenReturn(Instant.parse("2025-01-07T08:00:00Z"));

        perform(MockMvcRequestBuilders.post(REST_URL_RESTAURANT_1_ID_VOTE))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void voteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + NOT_FOUND))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.JUST_ADMIN_MAIL)
    void voteForbidden() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_RESTAURANT_1_ID_VOTE))
                .andExpect(status().isForbidden());
    }
}