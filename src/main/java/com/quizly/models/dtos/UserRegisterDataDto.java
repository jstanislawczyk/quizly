package com.quizly.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDataDto {

    @Email
    private String email;

    private String password;

    private String passwordRepeat;
}
