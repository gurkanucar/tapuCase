package com.gucardev.tapucase.service;

import com.gucardev.tapucase.exception.CodeAlreadyExistsException;
import com.gucardev.tapucase.exception.ExceptionMessages;
import com.gucardev.tapucase.exception.ShortUrlNotFoundException;
import com.gucardev.tapucase.model.ShortUrl;
import com.gucardev.tapucase.model.User;
import com.gucardev.tapucase.repository.ShortUrlRepository;
import com.gucardev.tapucase.request.CreateShortUrlRequest;
import com.gucardev.tapucase.util.EnvironmentData;
import com.gucardev.tapucase.util.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShortUrlService {

    private final ShortUrlRepository repository;
    private final UserService userService;
    private final RandomStringGenerator randomStringGenerator;


    public ShortUrl create(CreateShortUrlRequest createShortUrlRequest, Long id) {

        String code = createShortUrlRequest.getCode();
        log.info("Code: " + code);

           if (code == null || code.isEmpty()) {
               code = generateCode();
               log.info("Code generated automatically: " + code);
           } else if (repository.findByCode(code).isPresent()) {
               throw new CodeAlreadyExistsException(ExceptionMessages.SHORT_URL_ALREADY_EXISTS.getValue());
           }

           ShortUrl shortUrl = ShortUrl.builder()
                   .url(createShortUrlRequest.getUrl())
                   .user(userService.getUserById(id))
                   .code(code)
                   .build();
        return repository.save(shortUrl);
    }


    public ShortUrl getUrlByCode(String code) {
        return repository.findByCode(code).orElseThrow(
                () -> new ShortUrlNotFoundException(ExceptionMessages.SHORT_URL_NOT_FOUND.getValue())
        );
    }

    public ShortUrl getUrlById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ShortUrlNotFoundException(ExceptionMessages.SHORT_URL_NOT_FOUND.getValue())
        );
    }


    public List<ShortUrl> getAllByUserId(Long userId) {
        return repository.findAllByUser(userService.getUserById(userId));
    }

    public ShortUrl deleteById(Long url_id) {
        ShortUrl shortUrl = getUrlById(url_id);
        repository.delete(shortUrl);
        return shortUrl;
    }


    protected String generateCode() {
        String code;
        do {
            code = randomStringGenerator.generateRandomString().toUpperCase();
        }
        while (repository.findByCode(code).isPresent());
        return code;
    }


}
