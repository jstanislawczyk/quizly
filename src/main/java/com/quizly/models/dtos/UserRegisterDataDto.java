package com.quizly.models.dtos;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.Email;

@Builder
@Data
public class UserRegisterDataDto {

    @Email
    private String email;

    private char[] password;

    private char[] passwordRepeat;
}
