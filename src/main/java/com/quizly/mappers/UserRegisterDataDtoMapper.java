package com.quizly.mappers;

import com.quizly.models.common.UserRegisterData;
import com.quizly.models.dtos.UserRegisterDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisterDataDtoMapper {

    public UserRegisterData toModel(final UserRegisterDataDto userRegisterDataDto) {
        return UserRegisterData
                .builder()
                    .email(userRegisterDataDto.getEmail())
                    .password(userRegisterDataDto.getPassword())
                    .passwordRepeat(userRegisterDataDto.getPasswordRepeat())
                .build();
    }
}
