package com.gucardev.tapucase.controller;

import com.gucardev.tapucase.dto.UserDto;
import com.gucardev.tapucase.model.User;
import com.gucardev.tapucase.request.CreateUserRequest;
import com.gucardev.tapucase.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {


    private final ModelMapper mapper;
    private final UserService userService;


    @PostMapping("/signup")
    ResponseEntity<?> signupUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        User createdUser = userService.signupUser(mapper.map(createUserRequest, User.class));
        UserDto dto = mapper.map(createdUser, UserDto.class);
        log.info("Created User:" + dto.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


}
