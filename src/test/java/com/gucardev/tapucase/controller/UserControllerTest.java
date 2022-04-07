package com.gucardev.tapucase.controller;

import com.gucardev.tapucase.IntegrationTestSupport;
import com.gucardev.tapucase.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest extends IntegrationTestSupport {

    public String baseUrl = "/user";

    @Test
    public void testSignupUserWhenUsernameDoesNotExist() throws Exception {
        final var user = generateUser();

        String requestJson = mapper.writeValueAsString(generateUser());

        this.mockMvc.perform(post(baseUrl + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.username", is(user.getUsername())));

    }

    @Test
    public void testSignupUserWhenUsernameExists() throws Exception {
        final var user = generateUser();

        when(userRepository.findUserByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        String requestJson = mapper.writeValueAsString(generateUser());

        this.mockMvc.perform(post(baseUrl + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is4xxClientError())
                .andExpect(
                        result ->
                                assertTrue(result.getResolvedException()
                                        instanceof UserAlreadyExistsException));

    }


}