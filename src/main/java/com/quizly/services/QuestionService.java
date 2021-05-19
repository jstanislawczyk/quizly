package com.quizly.services;

import com.quizly.enums.QuestionType;
import com.quizly.exceptions.NotFoundException;
import com.quizly.models.entities.Answer;
import com.quizly.models.entities.Question;
import com.quizly.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> getRandomQuestionsList(final List<QuestionType> types, final int quantity) {
        final List<String> questionTypesStrings = types
                .stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        return this.questionRepository.findRandomQuestionsByTypeWithLimit(questionTypesStrings, quantity);
    }

    public Optional<Question> findQuestionById(final Long id) {
        return this.questionRepository.findById(id);
    }

    public List<Question> getQuestionsByIdsWithCorrectAnswers(final List<Question> questions) {
        return questions
                .stream()
                .map(question ->
                    Question.builder()
                        .id(question.getId())
                        .points(question.getPoints())
                        .questionType(question.getQuestionType())
                        .answers(
                            question.getAnswers()
                                .stream()
                                .filter(Answer::isCorrect)
                                .collect(Collectors.toList())
                        )
                        .build()
                )
                .collect(Collectors.toList());
    }

    public Question findQuestionByIdInList(final Long id, final List<Question> questions) {
        return questions
            .stream()
            .filter(question ->
                question.getId().equals(id)
            )
            .findAny()
            .orElseThrow(() ->
                new NotFoundException("Answer for question with id=" + id + " not found")
            );
    }

    public Question saveQuestion(final Question question) {
        log.info("Saving new question");

        return this.questionRepository.save(question);
    }

    public void deleteQuestionById(final Long id) {
        this.questionRepository.deleteById(id);
    }
}
