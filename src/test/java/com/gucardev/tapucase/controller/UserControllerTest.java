package com.gucardev.tapucase.controller;

import com.gucardev.tapucase.IntegrationTestSupport;
import com.gucardev.tapucase.exception.UserAlreadyExistsException;
import com.gucardev.tapucase.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@Slf4j
class UserControllerTest extends IntegrationTestSupport {


    @Test
    public void testSignupUserWhenUsernameDoesNotExist() throws Exception {
        final var user = generateUser();
        final var createUserRequest = generateCreateUserRequest(
                user.getUsername()
                , user.getPassword());
        String requestJson = mapper.writeValueAsString(createUserRequest);

        this.mockMvc.perform(post(USER_CONTROLLER_BASE_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())));

    }


    @Test
    public void testSignupUserWhenUsernameExists() throws Exception {
        final var user = generateUser();

        when(userRepository.findUserByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        String requestJson = mapper.writeValueAsString(generateUser());

        this.mockMvc.perform(post(USER_CONTROLLER_BASE_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is4xxClientError())
                .andExpect(
                        result ->
                                assertTrue(result.getResolvedException()
                                        instanceof UserAlreadyExistsException));

    }


}