package ru.diasoft.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import ru.diasoft.quiz.dao.QuestionLoader;
import ru.diasoft.quiz.model.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionLoaderTest {

    @Mock
    private Resource mockResource;

    @Mock
    private MessageSource mockMessageSource;

    private QuestionLoader questionLoader;

    @BeforeEach
    void setUp() {
        questionLoader = new QuestionLoader(mockResource, mockMessageSource);
    }

    @Test
    void testLoadQuestionsWithValidCsv() throws Exception {
        String csvContent = "Test question?;Option1;Option2;Option3;1";
        
        when(mockResource.getInputStream()).thenReturn(
            new java.io.ByteArrayInputStream(csvContent.getBytes())
        );

        List<Question> questions = questionLoader.loadQuestions();

        assertEquals(1, questions.size());
        Question question = questions.get(0);
        assertEquals("Test question?", question.getText());
        assertEquals(List.of("Option1", "Option2", "Option3"), question.getOptions());
        assertEquals(0, question.getCorrectIndex());
    }
}
