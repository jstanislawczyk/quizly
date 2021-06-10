package com.quizly.models.common;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserRegisterData {

    private String email;

    private char[] password;

    private char[] passwordRepeat;
}
