package com.quizly.mappers;

import com.quizly.models.dtos.UserDto;
import com.quizly.models.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDtoMapper {

    public UserDto toDto(final User user) {
        return UserDto
                .builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .role(user.getRole())
                .build();
    }
}
