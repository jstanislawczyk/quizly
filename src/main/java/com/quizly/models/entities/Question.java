package com.quizly.models.entities;

import com.quizly.enums.QuestionType;
import lombok.Builder;
import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 510)
    private String text;

    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private QuestionType questionType;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers;
}
