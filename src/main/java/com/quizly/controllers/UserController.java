package com.quizly.controllers;

import com.quizly.mappers.UserDtoMapper;
import com.quizly.mappers.UserRegisterDataDtoMapper;
import com.quizly.models.common.UserRegisterData;
import com.quizly.models.dtos.UserDto;
import com.quizly.models.dtos.UserRegisterDataDto;
import com.quizly.models.entities.User;
import com.quizly.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserRegisterDataDtoMapper userRegisterDataDtoMapper;
    private final UserDtoMapper userDtoMapper;

    @PostMapping("/register")
    @ResponseStatus(CREATED)
    public UserDto registerUser(
        @RequestBody @Valid final UserRegisterDataDto userRegisterDataDto
    ) {
        log.info("Registering new user");

        final UserRegisterData userRegisterData = this.userRegisterDataDtoMapper.toModel(userRegisterDataDto);
        final User user = this.userService.createUser(userRegisterData);

        return this.userDtoMapper.toDto(user);
    }
}
