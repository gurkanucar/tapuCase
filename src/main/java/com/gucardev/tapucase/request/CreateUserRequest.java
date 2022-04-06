package com.gucardev.tapucase.request;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserRequest {

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String password;


}
