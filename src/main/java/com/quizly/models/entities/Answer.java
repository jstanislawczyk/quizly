package com.quizly.models.entities;

import lombok.Builder;
import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Builder
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private char point;

    private String text;

    private boolean correct;

    @ManyToOne
    @JoinColumn
    private Question question;
}
