package com.gucardev.tapucase.controller;

import com.gucardev.tapucase.annotation.CheckOwner;
import com.gucardev.tapucase.dto.ShortUrlDto;
import com.gucardev.tapucase.model.ShortUrl;
import com.gucardev.tapucase.request.CreateShortUrlRequest;
import com.gucardev.tapucase.service.ShortUrlService;
import com.gucardev.tapucase.util.EnvironmentData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.net.URI;
import java.net.URISyntaxException;
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
        log.info("Code: " + code);
        ShortUrl shortUrl = shortUrlService.getUrlByCode(code);
        URI uri = new URI(shortUrl.getUrl());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).headers(httpHeaders).build();
    }

    @GetMapping("/user/{userId}/list")
    public ResponseEntity<List<ShortUrlDto>> getAllByUserId(HttpServletRequest httpServletRequest,
                                                            @Valid @NotEmpty @PathVariable Long userId) {
        List<ShortUrlDto> list = shortUrlService.getAllByUserId(userId)
                .stream().map(x -> mapper.map(x, ShortUrlDto.class))
                .peek(x -> x.setShortened(environmentData.getURLBase(httpServletRequest) + "/s/" + x.getCode()))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }


    @CheckOwner
    @GetMapping("/user/{userId}/detail/{urlId}")
    public ResponseEntity<ShortUrlDto> getDetailByUserIdAndUrlId(HttpServletRequest httpServletRequest,
                                                                 @Valid @NotEmpty @PathVariable Long userId,
                                                                 @Valid @NotEmpty @PathVariable Long urlId) {
        ShortUrlDto dto = mapper.map(shortUrlService.getUrlById(urlId), ShortUrlDto.class);
        dto.setShortened(environmentData.getURLBase(httpServletRequest) + "/s/" + dto.getCode());

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @CheckOwner
    @DeleteMapping("/user/{userId}/detail/{urlId}")
    public ResponseEntity<ShortUrlDto> deleteShortUrlById(HttpServletRequest httpServletRequest,
                                                          @Valid @NotEmpty @PathVariable Long userId,
                                                          @Valid @NotEmpty @PathVariable Long urlId) {
        ShortUrlDto dto = mapper.map(shortUrlService.deleteById(urlId), ShortUrlDto.class);
        dto.setShortened(environmentData.getURLBase(httpServletRequest) + "/s/" + dto.getCode());

        return ResponseEntity.ok(dto);
    }


}
