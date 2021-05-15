package com.quizly.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uniqueCode;

    private int quizDurationInSeconds;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private int correctQuestions;

    private int totalQuestions;

    @ManyToMany
    @JoinTable
    private List<Question> questions;
}
