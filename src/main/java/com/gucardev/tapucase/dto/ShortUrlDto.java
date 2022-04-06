package com.gucardev.tapucase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShortUrlDto {

    private Long id;
    private String url;
    private String code;
    private String shortened;


}
