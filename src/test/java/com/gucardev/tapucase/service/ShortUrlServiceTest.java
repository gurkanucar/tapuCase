package com.gucardev.tapucase.service;

import com.gucardev.tapucase.TestSupport;

import com.gucardev.tapucase.exception.CodeAlreadyExistsException;
import com.gucardev.tapucase.exception.ShortUrlNotFoundException;
import com.gucardev.tapucase.model.ShortUrl;
import com.gucardev.tapucase.repository.ShortUrlRepository;
import com.gucardev.tapucase.request.CreateShortUrlRequest;
import com.gucardev.tapucase.util.RandomStringGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class ShortUrlServiceTest extends TestSupport {


    private ShortUrlRepository shortUrlRepository;

    private UserService userService;

    private RandomStringGenerator randomStringGenerator;

    private ShortUrlService shortUrlService;

    @BeforeEach
    public void setUp() {
        shortUrlRepository = mock(ShortUrlRepository.class);
        userService = mock(UserService.class);
        randomStringGenerator = mock(RandomStringGenerator.class);
        shortUrlService = new ShortUrlService(shortUrlRepository, userService, randomStringGenerator);
    }

    @Test
    public void testCreateWhenCodeDoesNotExistThenSave() {
        final var expected = generateShortUrl();
        when(shortUrlRepository.findByCode(expected.getCode()))
                .thenReturn(Optional.empty());
        when(userService.getUserById(expected.getUser().getId()))
                .thenReturn(expected.getUser());
        when(shortUrlRepository.save(any())).thenReturn(expected);
        final var actual = shortUrlService.create(
                generateCreateShortUrlRequest(expected.getUrl(),
                        expected.getCode()),
                expected.getUser().getId()
        );
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateWhenCodeExistsThenThrowException() {
        final var expected = generateShortUrl();
        when(shortUrlRepository.findByCode(expected.getCode()))
                .thenReturn(Optional.of(expected));
        assertThrows(CodeAlreadyExistsException.class,
                () -> shortUrlService.create(generateCreateShortUrlRequest(expected.getUrl(), expected.getCode()), 1L));

        verify(shortUrlRepository, times(0)).save(expected);

    }


    @Test
    public void testGetUrlByCodeWhenCodeExistsThenReturnShortUrl() {
        var expected = generateShortUrl();

        when(shortUrlRepository.findByCode(expected.getCode()))
                .thenReturn(Optional.of(expected));

        var actual = shortUrlService.getUrlByCode(expected.getCode());
        verify(shortUrlRepository, times(1))
                .findByCode(expected.getCode());
        assertEquals(expected, actual);

    }

    @Test
    public void testGetUrlByCodeWhenCodeDoesNotExistThenThrowException() {
        var expected = generateShortUrl();
        when(shortUrlRepository.findByCode(expected.getCode()))
                .thenReturn(Optional.empty());

        assertThrows(ShortUrlNotFoundException.class,
                () -> shortUrlService.getUrlByCode("CODE"));

    }

    @Test
    public void testGenerateCode() {

        String expected = "CODE";

        when(randomStringGenerator.generateRandomString())
                .thenReturn(expected);

        var actual = shortUrlService.generateCode();
        assertEquals(expected, actual);

    }


}