package com.quizly.repositories;

import com.quizly.models.entities.Question;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(
        nativeQuery = true,
        value =
            """
            SELECT * FROM question
            WHERE question.question_type IN (:types)
            ORDER BY RAND()
            LIMIT :quantity
            """
    )
    List<Question> findRandomQuestionsByTypeWithLimit(
        @Param("types") final List<String> types,
        @Param("quantity") final int quantity
    );

    @EntityGraph(attributePaths = {"answers"})
    List<Question> findQuestionsByIdIn(final List<Long> ids);
}
