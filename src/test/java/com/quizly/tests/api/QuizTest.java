package com.quizly.tests.api;

import com.quizly.enums.QuestionType;
import com.quizly.models.dtos.QuizDto;
import com.quizly.models.entities.Question;
import com.quizly.models.entities.Quiz;
import com.quizly.repositories.QuestionRepository;
import com.quizly.repositories.QuizRepository;
import com.quizly.utils.ObjectUtils;
import com.quizly.utils.StringPatternUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class QuizTest extends ApiTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void createQuiz_ShouldCreateQuiz_Created() throws Exception {
        // Given
        final List<Question> existingQuestions = List.of(
                Question
                    .builder()
                        .points(1)
                        .text("Question 1")
                        .questionType(QuestionType.SINGLE_CHOICE)
                        .photoUrl("https://testurlofphoto1.com")
                    .build(),
                Question
                    .builder()
                        .points(2)
                        .text("Question 2")
                        .questionType(QuestionType.SINGLE_CHOICE)
                        .photoUrl("https://testurlofphoto2.com")
                    .build()
        );

        this.questionRepository.saveAll(existingQuestions);

        // When & Then
        mvc.perform(
                post("/quiz")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ObjectUtils.convertObjectToJsonBytes(QuizDto.builder().build()))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(not(nullValue()))))
                .andExpect(jsonPath("$.uniqueCode", Matchers.matchesPattern(StringPatternUtils.getUUIDPattern())))
                .andExpect(jsonPath("$.quizDurationInSeconds", is(0)))
                .andExpect(jsonPath("$.finishedAt", is(nullValue())))
                .andExpect(jsonPath("$.startedAt", is(not(nullValue()))))
                .andExpect(jsonPath("$.correctQuestions", is(0)))
                .andExpect(jsonPath("$.totalQuestions", is(10)))
                .andExpect(jsonPath("$.questions", hasSize(2)));

        final List<Quiz> quizzes = this.quizRepository.findAll();

        assertEquals(quizzes.size(), 1);
    }

    @Test
    void createQuiz_ShouldCreateQuizWithRequiredNumberOfQuestions_Created() throws Exception {
        // Given
        final List<Question> existingQuestions = List.of(
                Question
                    .builder()
                        .points(1)
                        .text("Question 1")
                        .questionType(QuestionType.SINGLE_CHOICE)
                        .photoUrl("https://testurlofphoto1.com")
                    .build(),
                Question
                    .builder()
                        .points(2)
                        .text("Question 2")
                        .questionType(QuestionType.SINGLE_CHOICE)
                        .photoUrl("https://testurlofphoto2.com")
                    .build(),
                Question
                    .builder()
                        .points(3)
                        .text("Question 3")
                        .questionType(QuestionType.SINGLE_CHOICE)
                        .photoUrl("https://testurlofphoto2.com")
                    .build()
        );

        this.questionRepository.saveAll(existingQuestions);

        // When & Then
        mvc.perform(
                post("/quiz?quantity=2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ObjectUtils.convertObjectToJsonBytes(QuizDto.builder().build()))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalQuestions", is(2)))
                .andExpect(jsonPath("$.questions", hasSize(2)));
    }

    @Test
    void createQuiz_ShouldCreateQuizWithRequiredTypesOfQuestion_Created() throws Exception {
        // Given
        final List<Question> existingQuestions = List.of(
                Question
                    .builder()
                        .points(1)
                        .text("Question 1")
                        .questionType(QuestionType.SINGLE_CHOICE)
                        .photoUrl("https://testurlofphoto1.com")
                    .build(),
                Question
                    .builder()
                        .points(2)
                        .text("Question 2")
                        .questionType(QuestionType.MULTI_CHOICE)
                        .photoUrl("https://testurlofphoto2.com")
                    .build(),
                Question
                    .builder()
                        .points(3)
                        .text("Question 3")
                        .questionType(QuestionType.OPEN)
                        .photoUrl("https://testurlofphoto3.com")
                    .build()
        );

        this.questionRepository.saveAll(existingQuestions);

        // When & Then
        mvc.perform(
                post("/quiz?types=SINGLE_CHOICE&types=MULTI_CHOICE")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ObjectUtils.convertObjectToJsonBytes(QuizDto.builder().build()))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.questions", hasSize(2)));
    }

    @Test
    void createQuiz_ShouldCreateQuizWithAllTypesOfQuestion_Created() throws Exception {
        // Given
        final List<Question> existingQuestions = List.of(
                Question
                    .builder()
                        .points(1)
                        .text("Question 1")
                        .questionType(QuestionType.SINGLE_CHOICE)
                        .photoUrl("https://testurlofphoto1.com")
                    .build(),
                Question
                    .builder()
                        .points(2)
                        .text("Question 2")
                        .questionType(QuestionType.MULTI_CHOICE)
                        .photoUrl("https://testurlofphoto2.com")
                    .build(),
                Question
                    .builder()
                        .points(3)
                        .text("Question 3")
                        .questionType(QuestionType.OPEN)
                        .photoUrl("https://testurlofphoto3.com")
                    .build()
        );

        this.questionRepository.saveAll(existingQuestions);

        // When & Then
        mvc.perform(
                post("/quiz?types=SINGLE_CHOICE&types=MULTI_CHOICE&types=OPEN")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ObjectUtils.convertObjectToJsonBytes(QuizDto.builder().build()))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.questions", hasSize(3)));
    }

    @Test
    void createQuiz_ShouldCreateQuizWithAllowedDuration_Created() throws Exception {
        // Given
        final QuizDto quizDto = QuizDto
                .builder()
                    .quizDurationInSeconds(60)
                .build();

        // When & Then
        mvc.perform(
                post("/quiz")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ObjectUtils.convertObjectToJsonBytes(quizDto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.quizDurationInSeconds", is(quizDto.getQuizDurationInSeconds())));
    }
}
