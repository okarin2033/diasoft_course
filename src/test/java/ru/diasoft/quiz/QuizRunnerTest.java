package ru.diasoft.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import ru.diasoft.quiz.model.Question;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuizRunnerTest {

    @Mock
    private QuestionLoader mockQuestionLoader;

    @Mock
    private MessageSource mockMessageSource;

    @Mock
    private Locale mockLocale;

    private QuizRunner quizRunner;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        quizRunner = new QuizRunner(mockQuestionLoader, mockMessageSource, mockLocale);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        when(mockMessageSource.getMessage("quiz.enter.name", null, mockLocale)).thenReturn("Enter name: ");
        when(mockMessageSource.getMessage("quiz.enter.lastname", null, mockLocale)).thenReturn("Enter lastname: ");
        when(mockMessageSource.getMessage("quiz.question.number", new Object[]{1}, mockLocale)).thenReturn("Question 1:");
        when(mockMessageSource.getMessage("quiz.enter.answer", null, mockLocale)).thenReturn("Your answer: ");
        when(mockMessageSource.getMessage("quiz.your.answer", new Object[]{1, 1}, mockLocale)).thenReturn("score: 1 out of 1");
    }

    @Test
    void testRunWithCorrectAnswer() {
        List<Question> questions = List.of(
            new Question("Test question?", List.of("Option 1", "Option 2"), 0)
        );
        
        when(mockQuestionLoader.loadQuestions()).thenReturn(questions);
        
        String input = "John\nDoe\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        quizRunner.run();

        String output = outputStream.toString();
        assertTrue(output.contains("Enter name:"));
        assertTrue(output.contains("Enter lastname:"));
        assertTrue(output.contains("John Doe, score: 1 out of 1"));
    }
}
