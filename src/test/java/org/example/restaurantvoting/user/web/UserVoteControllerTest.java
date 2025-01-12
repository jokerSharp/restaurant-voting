package org.example.restaurantvoting.user.web;

import org.example.restaurantvoting.AbstractControllerTest;
import org.example.restaurantvoting.MatcherFactory;
import org.example.restaurantvoting.common.util.JsonUtil;
import org.example.restaurantvoting.restaurant.RestaurantTestData;
import org.example.restaurantvoting.restaurant.repository.VoteRepository;
import org.example.restaurantvoting.restaurant.to.VoteTo;
import org.example.restaurantvoting.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.*;

import static org.example.restaurantvoting.restaurant.RestaurantTestData.RESTAURANT_1_ID;
import static org.example.restaurantvoting.restaurant.RestaurantTestData.RESTAURANT_2_ID;
import static org.example.restaurantvoting.user.VoteTestData.*;
import static org.example.restaurantvoting.user.web.UserVoteController.REST_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserVoteControllerTest extends AbstractControllerTest {

    private static final LocalTime VOTE_BEFORE_END_TIME = LocalTime.of(10, 0);
    private static final LocalTime VOTE_END_TIME = LocalTime.of(11, 0);
    private static final LocalDate VOTE_END_DATE = LocalDate.now();
    private static final LocalDateTime VOTE_END = LocalDateTime.of(VOTE_END_DATE, VOTE_END_TIME);
    private static final LocalDateTime VOTE_BEFORE_END = LocalDateTime.of(VOTE_END_DATE, VOTE_BEFORE_END_TIME);

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private Clock clock;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(VOTE_MATCHER.contentJson(vote1, vote2));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/current-day"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void vote() throws Exception {
        VoteTo newTo = new VoteTo(null, 1, LocalDate.now(clock));
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isNoContent());
        assertEquals(RESTAURANT_1_ID, voteRepository.getExisted(3).getRestaurant().getId());
    }

//    @Test
//    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
//    void reVotePositive() throws Exception {
//        ReflectionTestUtils.setField(clock, "instant", VOTE_BEFORE_END.toInstant(ZoneOffset.UTC));
//        ReflectionTestUtils.setField(clock, "zone", ZoneOffset.UTC);
//        VoteTo newTo = new VoteTo(2, 1);
//        perform(MockMvcRequestBuilders.put(REST_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(JsonUtil.writeValue(newTo)))
//                .andExpect(status().isNoContent());
//    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void reVoteNegative() throws Exception {
        VoteTo newTo = new VoteTo(2, 1, LocalDate.now(clock));
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void voteNotFound() throws Exception {
        VoteTo newTo = new VoteTo(null, RestaurantTestData.NOT_FOUND, LocalDate.now(clock));
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.JUST_ADMIN_MAIL)
    void voteForbidden() throws Exception {
        VoteTo newTo = new VoteTo(null, 1, LocalDate.now(clock));
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isForbidden());
    }

    @TestConfiguration
    static class CustomClockConfiguration {

        @Bean
        @Primary
        public Clock fixedClock() {
            return Clock.fixed(VOTE_END.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        }
    }
}