package com.quizly.models.entities;

import com.quizly.enums.Role;
import lombok.Builder;
import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private char[] password;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private Role role;
}
