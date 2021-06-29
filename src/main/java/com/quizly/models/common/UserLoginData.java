package com.quizly.models.common;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserLoginData {

    private String email;

    private char[] password;
}
