package com.gucardev.tapucase;

import com.gucardev.tapucase.model.User;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

public class TestSupport {


    public User generateUser() {
        return User.builder()
                .id(anyLong())
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


}
