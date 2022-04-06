package com.gucardev.tapucase.request;


import com.gucardev.tapucase.model.User;
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
public class CreateShortUrlRequest {

    @NotNull
    private String url;

    @NotNull
    private String code;

}
