package com.gucardev.tapucase.controller;

import com.gucardev.tapucase.IntegrationTestSupport;
import com.gucardev.tapucase.model.ShortUrl;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ShortUrlControllerTest extends IntegrationTestSupport {

    @Test
    public void testSignupUserWhenUsernameDoesNotExist() throws Exception {
        final var user = generateUser();
        String requestJsonUser = mapper.writeValueAsString(user);

        final var shotUrlCreateRequest = generateCreateShortUrlRequest("URL", "CODE");


        //Generate user
        this.mockMvc.perform(post(USER_CONTROLLER_BASE_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJsonUser))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())));


        //Create ShortUrl

        String requestJson = mapper.writeValueAsString(shotUrlCreateRequest);
        this.mockMvc.perform(post(SHORT_URL_CONTROLLER_BASE_URL + "/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.code", is(shotUrlCreateRequest.getCode())));

    }

    @Test
    public void testRedirect() throws Exception {

        final var shortUrl = generateShortUrl();

        final var user = generateUser();
        String requestJsonUser = mapper.writeValueAsString(user);

        final var shotUrlCreateRequest = generateCreateShortUrlRequest(shortUrl.getUrl(), shortUrl.getCode());


        //Generate user
        this.mockMvc.perform(post(USER_CONTROLLER_BASE_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJsonUser))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())));


        //Create ShortUrl
        String requestJson = mapper.writeValueAsString(shotUrlCreateRequest);
        this.mockMvc.perform(post(SHORT_URL_CONTROLLER_BASE_URL + "/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.code", is(shotUrlCreateRequest.getCode())));


        this.mockMvc.perform(get(SHORT_URL_CONTROLLER_BASE_URL + "/" + shortUrl.getCode())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());


    }


    @Test
    public void testGetAllByUserId() throws Exception {

        final var shortUrl = generateShortUrl();

        final var user = generateUser();
        String requestJsonUser = mapper.writeValueAsString(user);

        final var shotUrlCreateRequest = generateCreateShortUrlRequest(shortUrl.getUrl(), shortUrl.getCode());
        final var shotUrlCreateRequest2 = generateCreateShortUrlRequest(shortUrl.getUrl(), shortUrl.getCode() + "#123");


        //Generate user
        this.mockMvc.perform(post(USER_CONTROLLER_BASE_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJsonUser))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())));


        //Create ShortUrl
        String requestJson = mapper.writeValueAsString(shotUrlCreateRequest);
        this.mockMvc.perform(post(SHORT_URL_CONTROLLER_BASE_URL + "/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.code", is(shotUrlCreateRequest.getCode())));

        //Create ShortUrl2
        String requestJson2 = mapper.writeValueAsString(shotUrlCreateRequest2);
        this.mockMvc.perform(post(SHORT_URL_CONTROLLER_BASE_URL + "/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson2))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.code", is(shotUrlCreateRequest2.getCode())));


        //Get All by userId
        this.mockMvc.perform(get(SHORT_URL_CONTROLLER_BASE_URL + "/user/" + user.getId() + "/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.*", hasSize(2)));


    }


}