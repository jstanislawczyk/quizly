package com.quizly.models.dtos;

import com.quizly.enums.Role;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDto {

    private Long id;

    @Email
    private String email;

    private char[] password;

    @Size(min = 3, max = 100, message = "Firstname must have size between {min} and {max} letters")
    private String firstName;

    @Size(min = 3, max = 100, message = "Lastname must have size between {min} and {max} letters")
    private String lastName;

    private Role role;
}
