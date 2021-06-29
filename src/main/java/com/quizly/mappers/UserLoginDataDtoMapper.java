package com.quizly.mappers;

import com.quizly.models.common.UserLoginData;
import com.quizly.models.dtos.UserLoginDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserLoginDataDtoMapper {

    public UserLoginData toModel(final UserLoginDataDto userLoginDataDto) {
        return UserLoginData
                .builder()
                    .email(userLoginDataDto.getEmail())
                    .password(userLoginDataDto.getPassword().toCharArray())
                .build();
    }
}
