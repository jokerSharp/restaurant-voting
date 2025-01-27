package org.example.restaurantvoting.user.web;

import org.example.restaurantvoting.AbstractControllerTest;
import org.example.restaurantvoting.common.util.JsonUtil;
import org.example.restaurantvoting.restaurant.RestaurantTestData;
import org.example.restaurantvoting.restaurant.repository.VoteRepository;
import org.example.restaurantvoting.restaurant.service.VoteService;
import org.example.restaurantvoting.restaurant.to.VoteTo;
import org.example.restaurantvoting.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.*;

import static org.example.restaurantvoting.restaurant.RestaurantTestData.RESTAURANT_1_ID;
import static org.example.restaurantvoting.user.VoteTestData.*;
import static org.example.restaurantvoting.user.web.UserVoteController.BY_DATE_URL;
import static org.example.restaurantvoting.user.web.UserVoteController.REST_URL;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserVoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL_CURRENT_DAY = REST_URL + BY_DATE_URL;

    private static final LocalTime VOTE_BEFORE_END_TIME = LocalTime.of(10, 0);
    private static final LocalTime VOTE_END_TIME = LocalTime.of(11, 0);
    private static final LocalDate VOTE_DATE = LocalDate.now();
    private static final LocalDateTime VOTE_END = LocalDateTime.of(VOTE_DATE, VOTE_END_TIME);
    private static final LocalDateTime VOTE_BEFORE_END = LocalDateTime.of(VOTE_DATE, VOTE_BEFORE_END_TIME);

    @Autowired
    private VoteRepository voteRepository;

    @MockitoBean
    private Clock clock;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getCurrentDayVote() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_CURRENT_DAY))
                .andExpect(status().isOk())
                .andExpect(VOTE_TO_MATCHER.contentJson(vote2));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(VOTE_TO_MATCHER.contentJson(vote1, vote2));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_CURRENT_DAY))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void vote() throws Exception {
        when(clock.instant()).thenReturn(VOTE_BEFORE_END.toInstant(ZoneOffset.UTC));
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);

        VoteTo newTo = new VoteTo(null, 1, LocalDate.now(clock));
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isNoContent());
        assertEquals(RESTAURANT_1_ID, voteRepository.getExisted(3).getRestaurant().getId());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void voteNextDay() throws Exception {
        when(clock.instant()).thenReturn(VOTE_END.toInstant(ZoneOffset.UTC));
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);

        VoteTo newTo = new VoteTo(null, 1, LocalDate.now(clock).plusDays(1));
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(VoteService.EXCEPTION_VOTE_OTHER_DAY)));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void reVotePositive() throws Exception {
        when(clock.instant()).thenReturn(VOTE_BEFORE_END.toInstant(ZoneOffset.UTC));
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);

        VoteTo newTo = new VoteTo(2, 1, LocalDate.now(clock));
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void reVoteAfterEndTime() throws Exception {
        when(clock.instant()).thenReturn(VOTE_END.toInstant(ZoneOffset.UTC));
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);

        VoteTo newTo = new VoteTo(2, 1, LocalDate.now(clock));
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(VoteService.EXCEPTION_VOTE_AFTER_END_TIME)));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void reVotePreviousDay() throws Exception {
        when(clock.instant()).thenReturn(VOTE_END.toInstant(ZoneOffset.UTC));
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);

        VoteTo newTo = new VoteTo(1, 1, LocalDate.of(2025, Month.JANUARY, 10));
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(VoteService.EXCEPTION_VOTE_OTHER_DAY)));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void voteNotFound() throws Exception {
        when(clock.instant()).thenReturn(VOTE_BEFORE_END.toInstant(ZoneOffset.UTC));
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);

        VoteTo newTo = new VoteTo(null, RestaurantTestData.NOT_FOUND, LocalDate.now(clock));
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.JUST_ADMIN_MAIL)
    void voteForbidden() throws Exception {
        when(clock.instant()).thenReturn(VOTE_BEFORE_END.toInstant(ZoneOffset.UTC));
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);

        VoteTo newTo = new VoteTo(null, 1, LocalDate.now(clock));
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isForbidden());
    }
}