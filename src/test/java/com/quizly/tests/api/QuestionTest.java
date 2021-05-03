package com.quizly.tests.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizly.enums.QuestionType;
import com.quizly.models.entities.Question;
import com.quizly.repositories.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
                    .text("Question 1")
                    .questionType(QuestionType.SINGLE_CHOICE)
                    .photoUrl("http://testurlofphoto1.com")
                .build(),
            Question
                .builder()
                    .text("Question 2")
                    .questionType(QuestionType.MULTI_CHOICE)
                    .photoUrl("http://testurlofphoto2.com")
                .build(),
            Question
                .builder()
                    .text("Question 3")
                    .questionType(QuestionType.OPEN)
                    .photoUrl("http://testurlofphoto3.com")
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
                    .text("Question 1")
                    .questionType(QuestionType.SINGLE_CHOICE)
                    .photoUrl("http://testurlofphoto1.com")
                .build(),
            Question
                .builder()
                    .text("Question 2")
                    .questionType(QuestionType.MULTI_CHOICE)
                    .photoUrl("http://testurlofphoto2.com")
                .build(),
            Question
                .builder()
                    .text("Question 3")
                    .questionType(QuestionType.OPEN)
                    .photoUrl("http://testurlofphoto3.com")
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
                    .text("Question 1")
                    .questionType(QuestionType.SINGLE_CHOICE)
                    .photoUrl("http://testurlofphoto1.com")
                .build(),
            Question
                .builder()
                    .text("Question 2")
                    .questionType(QuestionType.SINGLE_CHOICE)
                    .photoUrl("http://testurlofphoto2.com")
                .build(),
            Question
                .builder()
                    .text("Question 3")
                    .questionType(QuestionType.SINGLE_CHOICE)
                    .photoUrl("http://testurlofphoto3.com")
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

    private List<Question> getOrderedQuestionsList(final String json) throws JsonProcessingException {
        final List<Question> returnedQuestions = Arrays.asList(new ObjectMapper().readValue(json, Question[].class));
        returnedQuestions.sort(Comparator.comparing(Question::getId));

        return returnedQuestions;
    }
}
