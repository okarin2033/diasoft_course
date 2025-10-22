package ru.diasoft.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import ru.diasoft.quiz.model.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionLoaderTest {

    @Mock
    private Resource mockResource;

    private QuestionLoader questionLoader;

    @BeforeEach
    void setUp() {
        questionLoader = new QuestionLoader(mockResource);
    }

    @Test
    void testLoadQuestionsWithValidCsv() throws Exception {
        String csvContent = "Какой язык программирования используется в Spring Boot?;Java;Python;JavaScript;1";
        
        when(mockResource.getInputStream()).thenReturn(
            new java.io.ByteArrayInputStream(csvContent.getBytes())
        );

        List<Question> questions = questionLoader.loadQuestions();

        assertEquals(1, questions.size());
        Question question = questions.get(0);
        assertEquals("Какой язык программирования используется в Spring Boot?", question.getText());
        assertEquals(List.of("Java", "Python", "JavaScript"), question.getOptions());
        assertEquals(0, question.getCorrectIndex());
    }

    @Test
    void testLoadQuestionsWithInvalidLines() throws Exception {
        String csvContent = "Какой язык программирования используется в Spring Boot?;Java;Python;JavaScript;1\nНеполная строка;Вариант1;Вариант2";
        
        when(mockResource.getInputStream()).thenReturn(
            new java.io.ByteArrayInputStream(csvContent.getBytes())
        );

        List<Question> questions = questionLoader.loadQuestions();

        assertEquals(1, questions.size());
    }

    @Test
    void testLoadQuestionsWithIOException() throws Exception {
        when(mockResource.getInputStream()).thenThrow(new java.io.IOException("Test exception"));

        assertThrows(IllegalStateException.class, () -> {
            questionLoader.loadQuestions();
        });
    }
}
