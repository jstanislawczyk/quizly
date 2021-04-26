package com.quizly.models.entities;

import lombok.Builder;
import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Builder
public class Answer {

    @Id
    @GeneratedValue
    private Long id;

    private char point;

    private String text;

    private boolean correct;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Question question;
}
