package com.gucardev.tapucase.controller;

import com.gucardev.tapucase.annotation.CheckOwner;
import com.gucardev.tapucase.dto.ShortUrlDto;
import com.gucardev.tapucase.model.ShortUrl;
import com.gucardev.tapucase.model.User;
import com.gucardev.tapucase.request.CreateShortUrlRequest;
import com.gucardev.tapucase.service.ShortUrlService;
import com.gucardev.tapucase.util.EnvironmentData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/s")
@RequiredArgsConstructor
@Slf4j
public class ShortUrlController {

    private final ModelMapper mapper;
    private final ShortUrlService shortUrlService;
    private final EnvironmentData environmentData;


    @PostMapping("/user/{id}")
    ResponseEntity<?> createShortUrl(HttpServletRequest httpServletRequest,
                                     @Valid @RequestBody CreateShortUrlRequest createShortUrlRequest,
                                     @PathVariable Long id) {
        log.info("CreateShortUrl Model:" + createShortUrlRequest.toString());
        log.info("User ID:" + id.toString());

        ShortUrl created = shortUrlService.create(createShortUrlRequest, id);
        ShortUrlDto dto = mapper.map(created, ShortUrlDto.class);
        dto.setShortened(environmentData.getURLBase(httpServletRequest) + "/s/" + dto.getCode());

        log.info("Short Url created: " + dto.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


    @GetMapping("/{code}")
    public ResponseEntity<ShortUrlDto> redirect(@Valid @NotEmpty @PathVariable String code) throws URISyntaxException {
        ShortUrl shortUrl = shortUrlService.getUrlByCode(code);
        URI uri = new URI(shortUrl.getUrl());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).headers(httpHeaders).build();
    }

    @GetMapping("/user/{user_id}/list")
    public ResponseEntity<List<ShortUrlDto>> getAllByUserId(HttpServletRequest httpServletRequest,
                                                            @Valid @NotEmpty @PathVariable Long user_id) {
        List<ShortUrlDto> list = shortUrlService.getAllByUserId(user_id)
                .stream().map(x -> mapper.map(x, ShortUrlDto.class))
                .peek(x -> x.setShortened(environmentData.getURLBase(httpServletRequest) + "/s/" + x.getCode()))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.FOUND).body(list);
    }


    @CheckOwner
    @GetMapping("/user/{user_id}/detail/{url_id}")
    public ResponseEntity<ShortUrlDto> getDetailByUserIdAndUrlId(@Valid @NotEmpty @PathVariable Long user_id,
                                                                 @Valid @NotEmpty @PathVariable Long url_id) {
        ShortUrlDto dto = mapper.map(shortUrlService.getUrlById(url_id), ShortUrlDto.class);
        return ResponseEntity.status(HttpStatus.FOUND).body(dto);
    }

    @CheckOwner
    @DeleteMapping("/user/{user_id}/detail/{url_id}")
    public ResponseEntity<ShortUrlDto> deleteShortUrlById(@Valid @NotEmpty @PathVariable Long user_id,
                                                          @Valid @NotEmpty @PathVariable Long url_id) {
        ShortUrlDto dto = mapper.map(shortUrlService.deleteById(url_id), ShortUrlDto.class);
        return ResponseEntity.ok(dto);
    }


}
