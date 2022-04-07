package com.gucardev.tapucase.service;

import com.gucardev.tapucase.TestSupport;
import com.gucardev.tapucase.exception.UserAlreadyExistsException;
import com.gucardev.tapucase.exception.UserNotFoundException;
import com.gucardev.tapucase.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest extends TestSupport {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    public void testSignupUserWhenUsernameDoesNotExistThenSave() {
        final var expected = generateUser();
        when(userRepository.save(expected))
                .thenReturn(expected);
        when(userRepository.findUserByUsername(expected.getUsername()))
                .thenReturn(Optional.empty());

        final var actual = userService.signupUser(expected);
        assertEquals(expected, actual);
        verify(userRepository).save(expected);
        verify(userRepository).findUserByUsername(expected.getUsername());
    }

    @Test
    public void testSignupUserWhenUsernameExistsThenThrowError() {
        final var expected = generateUser(1L, "grkn", "pass");

        when(userRepository.findUserByUsername(expected.getUsername()))
                .thenReturn(Optional.ofNullable(generateUser(321L, "grkn", "pass")));

        Throwable exception = Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> userService.signupUser(expected));

        Assertions.assertEquals(UserAlreadyExistsException.class, exception.getClass());

        verify(userRepository, times(0)).save(expected);
        verify(userRepository).findUserByUsername(expected.getUsername());
    }


    @Test
    public void testGetUserWhenUserIdExistsThenGetUser() {
        final var expected = generateUser(1L, "grkn", "pass");

        when(userRepository.findUserById(expected.getId()))
                .thenReturn(Optional.of(expected));

        final var actual = userService.getUserById(expected.getId());

        assertEquals(expected.getId(), actual.getId());
       verify(userRepository).findUserById(expected.getId());
    }

    @Test
    public void testGetUserWhenUserIdDoesNotExistThenThrowError() {
        final var expected = generateUser(1L, "grkn", "pass");

        when(userRepository.findUserById(expected.getId()))
                .thenReturn(Optional.empty());

        Throwable exception = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(expected.getId()));

        Assertions.assertEquals(UserNotFoundException.class, exception.getClass());
        verify(userRepository).findUserById(expected.getId());
    }

}