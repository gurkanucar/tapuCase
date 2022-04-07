package com.gucardev.tapucase;

import com.gucardev.tapucase.model.ShortUrl;
import com.gucardev.tapucase.model.User;
import com.gucardev.tapucase.request.CreateShortUrlRequest;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

public class TestSupport {


    public User generateUser() {
        return User.builder()
                .id(1L)
                .username("gurkan")
                .password("password")
                .build();
    }

    public User generateUser(Long id,String username,String password) {
        return User.builder()
                .id(id)
                .username(username)
                .password(password)
                .build();
    }


    public CreateShortUrlRequest generateCreateShortUrlRequest(String url,String code){
        return CreateShortUrlRequest.builder()
                .code(code)
                .url(url)
                .build();
    }

    public ShortUrl generateShortUrl(){
        return ShortUrl.builder()
                .id(1L)
                .code("CODE")
                .url("URL")
                .user(generateUser())
                .build();
    }



}
