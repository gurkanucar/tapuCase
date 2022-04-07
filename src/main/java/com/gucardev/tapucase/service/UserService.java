package com.gucardev.tapucase.service;

import com.gucardev.tapucase.exception.ExceptionMessages;
import com.gucardev.tapucase.exception.UserAlreadyExistsException;
import com.gucardev.tapucase.exception.UserNotFoundException;
import com.gucardev.tapucase.model.User;
import com.gucardev.tapucase.repository.UserRepository;
import com.gucardev.tapucase.util.Hasher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User signupUser(User user) {

        if (isUserExists(user.getUsername())) {
            throw new UserAlreadyExistsException(ExceptionMessages.USER_ALREADY_EXISTS.getValue());
        }

        //password hashed
        user.setPassword(Hasher.hash(user.getPassword()));
        userRepository.save(user);
        return user;
    }


    public boolean checkUserById(Long id) {
        return getUserById(id) != null;
    }

    public boolean isUserExists(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }

    public User getUserById(Long id) {
        return userRepository.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND.getValue()));
    }

}
