package com.gucardev.tapucase.service;

import com.gucardev.tapucase.TestSupport;
import com.gucardev.tapucase.exception.UserAlreadyExistsException;
import com.gucardev.tapucase.exception.UserNotFoundException;
import com.gucardev.tapucase.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest extends TestSupport {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    public void testSignupUserWhenUsernameDoesNotExistThenSave() {
        final var expected = generateUser();
        Mockito.when(userRepository.save(expected))
                .thenReturn(expected);

        Mockito.when(userRepository.findUserByUsername(expected.getUsername()))
                .thenReturn(Optional.empty());

        final var actual = userService.signupUser(expected);
        assertEquals(expected, actual);
        Mockito.verify(userRepository).save(expected);
        Mockito.verify(userRepository).findUserByUsername(expected.getUsername());
    }

    @Test
    public void testSignupUserWhenUsernameExistsThenThrowError() {
        final var expected = generateUser(1L, "grkn", "pass");

        Mockito.when(userRepository.findUserByUsername(expected.getUsername()))
                .thenReturn(Optional.ofNullable(generateUser(321L, "grkn", "pass")));

        Throwable exception = Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> userService.signupUser(expected));

        Assertions.assertEquals(UserAlreadyExistsException.class, exception.getClass());

        Mockito.verify(userRepository, Mockito.times(0)).save(expected);
        Mockito.verify(userRepository).findUserByUsername(expected.getUsername());
    }


    @Test
    public void testGetUserWhenUserIdExistsThenGetUser() {
        final var expected = generateUser(1L, "grkn", "pass");

        Mockito.when(userRepository.findUserById(expected.getId()))
                .thenReturn(Optional.of(expected));

        final var actual = userService.getUserById(expected.getId());

        assertEquals(expected.getId(), actual.getId());
        Mockito.verify(userRepository).findUserById(expected.getId());
    }

    @Test
    public void testGetUserWhenUserIdDoesNotExistThenThrowError() {
        final var expected = generateUser(1L, "grkn", "pass");

        Mockito.when(userRepository.findUserById(expected.getId()))
                .thenReturn(Optional.empty());

        Throwable exception = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(expected.getId()));

        Assertions.assertEquals(UserNotFoundException.class, exception.getClass());
        Mockito.verify(userRepository).findUserById(expected.getId());
    }


}