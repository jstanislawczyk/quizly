package com.quizly.models.entities;

import com.quizly.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int points;

    @Column(length = 510)
    private String text;

    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private QuestionType questionType;

    @ManyToMany(mappedBy = "questions")
    private List<Quiz> quizzes;

    @OneToMany(
        mappedBy = "question",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Answer> answers;
}
