package com.quizly.tests.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizly.enums.QuestionType;
import com.quizly.models.dtos.AnswerDto;
import com.quizly.models.dtos.QuestionDto;
import com.quizly.models.entities.Question;
import com.quizly.repositories.QuestionRepository;
import com.quizly.utils.ObjectUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class QuestionTest extends ApiTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void getQuestions_ShouldGetEmptyList_Ok() throws Exception {
        // When & Then
        mvc.perform(
                get("/questions")
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getQuestions_ShouldGetAllTypes_Ok() throws Exception {
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

        // When
        final String result = mvc.perform(
                get("/questions?types=SINGLE_CHOICE&types=MULTI_CHOICE&types=OPEN")
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Then
        final List<Question> returnedQuestions = this.getOrderedQuestionsList(result);

        assertEquals(returnedQuestions.get(0), existingQuestions.get(0));
        assertEquals(returnedQuestions.get(1), existingQuestions.get(1));
        assertEquals(returnedQuestions.get(2), existingQuestions.get(2));
    }

    @Test
    void getQuestions_ShouldGetOnlyRequestedTypes_Ok() throws Exception {
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
                    .points(1)
                    .text("Question 2")
                    .questionType(QuestionType.MULTI_CHOICE)
                    .photoUrl("https://testurlofphoto2.com")
                .build(),
            Question
                .builder()
                    .points(1)
                    .text("Question 3")
                    .questionType(QuestionType.OPEN)
                    .photoUrl("https://testurlofphoto3.com")
                .build()
        );

        this.questionRepository.saveAll(existingQuestions);

        // When
        final String result = mvc.perform(
                get("/questions?types=SINGLE_CHOICE&types=MULTI_CHOICE")
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Then
        final List<Question> returnedQuestions = this.getOrderedQuestionsList(result);

        assertEquals(returnedQuestions.get(0), existingQuestions.get(0));
        assertEquals(returnedQuestions.get(1), existingQuestions.get(1));
    }

    @Test
    void getQuestions_ShouldGetRequestedNumberOfQuestions_Ok() throws Exception {
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
                    .points(1)
                    .text("Question 2")
                    .questionType(QuestionType.SINGLE_CHOICE)
                    .photoUrl("https://testurlofphoto2.com")
                .build(),
            Question
                .builder()
                    .points(1)
                    .text("Question 3")
                    .questionType(QuestionType.SINGLE_CHOICE)
                    .photoUrl("https://testurlofphoto3.com")
                .build()
        );

        this.questionRepository.saveAll(existingQuestions);

        // When & Then
        mvc.perform(
                get("/questions?types=SINGLE_CHOICE&quantity=2")
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void saveQuestion_ShouldFailDtoValidation_BadRequest() throws Exception {
        // Given
        final QuestionDto newQuestion = QuestionDto
                .builder()
                    .points(1)
                    .text("Question 1")
                    .photoUrl("U")
                    .questionType(QuestionType.SINGLE_CHOICE)
                .build();

        // When & Then
        mvc.perform(
                post("/questions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ObjectUtils.convertObjectToJsonBytes(newQuestion))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveQuestion_ShouldFailValidationForSingleChoiceQuestion_BadRequest() throws Exception {
        // Given
        final List<AnswerDto> answers = List.of(
            AnswerDto
                .builder()
                    .point('A')
                    .text("Answer A")
                    .correct(true)
                .build(),
            AnswerDto
                .builder()
                    .point('B')
                    .text("Answer B")
                    .correct(true)
                .build()
        );
        final QuestionDto newQuestion = QuestionDto
                .builder()
                    .points(1)
                    .text("Question 1")
                    .photoUrl("https://testurlofphoto3.com")
                    .questionType(QuestionType.SINGLE_CHOICE)
                    .answers(answers)
                .build();

        // When & Then
        mvc.perform(
                post("/questions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ObjectUtils.convertObjectToJsonBytes(newQuestion))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("Single choice question must contain one correct answer")));
    }

    @Test
    void saveQuestion_ShouldFailValidationForMultiChoiceQuestion_BadRequest() throws Exception {
        // Given
        final List<AnswerDto> answers = List.of(
            AnswerDto
                .builder()
                    .point('A')
                    .text("Answer A")
                    .correct(false)
                .build(),
            AnswerDto
                .builder()
                    .point('B')
                    .text("Answer B")
                    .correct(false)
                .build(),
            AnswerDto
                .builder()
                    .point('C')
                    .text("Answer C")
                    .correct(false)
                .build()
        );
        final QuestionDto newQuestion = QuestionDto
                .builder()
                    .points(1)
                    .text("Question 1")
                    .photoUrl("https://testurlofphoto3.com")
                    .questionType(QuestionType.MULTI_CHOICE)
                    .answers(answers)
                .build();

        // When & Then
        mvc.perform(
                post("/questions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ObjectUtils.convertObjectToJsonBytes(newQuestion))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("Multi choice question must contain at least one correct answer")));
    }

    @Test
    void saveQuestion_ShouldFailValidationForOpenQuestion_BadRequest() throws Exception {
        // Given
        final List<AnswerDto> answers = List.of(
                AnswerDto
                    .builder()
                        .point('A')
                        .text("Answer A")
                        .correct(true)
                    .build()
        );
        final QuestionDto newQuestion = QuestionDto
                .builder()
                    .points(1)
                    .text("Question 1")
                    .photoUrl("https://testurlofphoto3.com")
                    .questionType(QuestionType.OPEN)
                    .answers(answers)
                .build();

        // When & Then
        mvc.perform(
                post("/questions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ObjectUtils.convertObjectToJsonBytes(newQuestion))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("Open question cannot have answers")));
    }

    @Test
    void saveQuestion_ShouldSave_Created() throws Exception {
        // Given
        final List<AnswerDto> answers = List.of(
            AnswerDto
                .builder()
                    .point('A')
                    .text("Answer A")
                    .correct(true)
                .build(),
            AnswerDto
                .builder()
                    .point('B')
                    .text("Answer B")
                    .correct(false)
                .build()
        );
        final QuestionDto newQuestion = QuestionDto
                .builder()
                    .points(1)
                    .text("Question 1")
                    .photoUrl("https://testurlofphoto3.com")
                    .questionType(QuestionType.SINGLE_CHOICE)
                    .answers(answers)
                .build();

        // When & Then
        mvc.perform(
                post("/questions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ObjectUtils.convertObjectToJsonBytes(newQuestion))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text", is(newQuestion.getText())))
                .andExpect(jsonPath("$.photoUrl", is(newQuestion.getPhotoUrl())))
                .andExpect(jsonPath("$.questionType", is(newQuestion.getQuestionType().name())))
                .andExpect(jsonPath("$.answers", hasSize(2)))
                .andExpect(jsonPath("$.answers[0].point", is(String.valueOf(newQuestion.getAnswers().get(0).getPoint()))))
                .andExpect(jsonPath("$.answers[0].text", is(newQuestion.getAnswers().get(0).getText())))
                .andExpect(jsonPath("$.answers[0].correct", is(true)))
                .andExpect(jsonPath("$.answers[1].point", is(String.valueOf(newQuestion.getAnswers().get(1).getPoint()))))
                .andExpect(jsonPath("$.answers[1].text", is(newQuestion.getAnswers().get(1).getText())))
                .andExpect(jsonPath("$.answers[1].correct", is(false)));
    }

    @Test
    void deleteQuestion_ShouldNotFoundNotExistingQuestion_NotFound() throws Exception {
        // When & Then
        mvc.perform(
                delete("/questions/1111")
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("Question with id=1111 not found")));
    }

    @Test
    void deleteQuestion_ShouldNotFoundNotExistingQuestion_NoContent() throws Exception {
        // Given
        final Question existingQuestion =
                Question
                    .builder()
                        .points(1)
                        .text("Question 1")
                        .questionType(QuestionType.SINGLE_CHOICE)
                        .photoUrl("http://testurlofphoto1.com")
                    .build();

        this.questionRepository.save(existingQuestion);

        // When & Then
        mvc.perform(
                delete("/questions/{id}", existingQuestion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    private List<Question> getOrderedQuestionsList(final String json) throws JsonProcessingException {
        final List<Question> returnedQuestions = Arrays.asList(new ObjectMapper().readValue(json, Question[].class));
        returnedQuestions.sort(Comparator.comparing(Question::getId));

        return returnedQuestions;
    }
}
