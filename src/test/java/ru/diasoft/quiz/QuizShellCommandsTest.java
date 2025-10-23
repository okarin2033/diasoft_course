package ru.diasoft.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import ru.diasoft.quiz.io.QuizInputOutput;
import ru.diasoft.quiz.model.Question;
import ru.diasoft.quiz.controller.QuizShellCommands;
import ru.diasoft.quiz.model.QuizResult;
import ru.diasoft.quiz.service.QuizService;
import ru.diasoft.quiz.service.QuizValidator;

import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class QuizShellCommandsTest {

    private QuizShellCommands quizShellCommands;

    @BeforeEach
    void setUp() {
        QuizService quizService = mock(QuizService.class);
        QuizInputOutput io = mock(QuizInputOutput.class);
        MessageSource messageSource = mock(MessageSource.class);
        QuizValidator quizValidator = mock(QuizValidator.class);
        when(quizValidator.validateNames(any(), any())).thenReturn(java.util.Optional.empty());

        Question question1 = new Question("Capital of France?", 
            List.of("Paris", "London", "Berlin"), 0);
        Question question2 = new Question("2+2=?", 
            List.of("3", "4", "5"), 1);

        when(quizService.getAllQuestions()).thenReturn(List.of(question1, question2));
        when(quizService.getQuestionsCount()).thenReturn(2);
        when(quizService.formatStats()).thenReturn("Quiz contains 2 questions.");
        when(quizService.formatAllQuestions()).thenReturn(
            "Available questions (2 total):\n" +
            "Question 1: Capital of France?\n" +
            "  1) Paris ✓\n" +
            "  2) London\n\n" +
            "Question 2: 2+2=?\n" +
            "  1) 3\n" +
            "  2) 4 ✓\n");
        
        QuizResult mockResult = new QuizResult(2, 2, "John Doe, Your score: 2 out of 2");
        when(quizService.runQuiz(any(), any(), any())).thenReturn(mockResult);

        when(messageSource.getMessage(eq("quiz.your.answer"), any(), eq(Locale.getDefault())))
            .thenReturn("Your score: {0} out of {1}");
        when(messageSource.getMessage(eq("quiz.enter.name"), any(), eq(Locale.getDefault())))
            .thenReturn("Enter your name:");
        when(messageSource.getMessage(eq("quiz.enter.lastname"), any(), eq(Locale.getDefault())))
            .thenReturn("Enter your lastname:");

        quizShellCommands = new QuizShellCommands(quizService, io, messageSource, Locale.getDefault(), quizValidator);
    }

    @Test
    void testStatsCommand() {
        String result = quizShellCommands.stats();
        assertEquals("Quiz contains 2 questions.", result);
    }

    @Test
    void testShowQuestionsCommand() {
        String result = quizShellCommands.showQuestions();
        
        assertTrue(result.contains("Available questions (2 total):"));
        assertTrue(result.contains("Question 1: Capital of France?"));
        assertTrue(result.contains("Question 2: 2+2=?"));
        assertTrue(result.contains("1) Paris ✓"));
        assertTrue(result.contains("2) London"));
        assertTrue(result.contains("2) 4 ✓"));
    }

    @Test
    void testQuizCommandWithEmptyName() {
        QuizValidator quizValidator = mock(QuizValidator.class);
        QuizService quizService = mock(QuizService.class);
        QuizInputOutput io = mock(QuizInputOutput.class);
        MessageSource messageSource = mock(MessageSource.class);
        when(quizValidator.validateNames(eq(""), any())).thenReturn(java.util.Optional.of("Error: First name is required. Use --name parameter."));
        QuizShellCommands local = new QuizShellCommands(quizService, io, messageSource, Locale.getDefault(), quizValidator);
        String result = local.quiz("", "Doe");
        assertEquals("Error: First name is required. Use --name parameter.", result);
    }

    @Test
    void testQuizCommandWithEmptyLastName() {
        QuizValidator quizValidator = mock(QuizValidator.class);
        QuizService quizService = mock(QuizService.class);
        QuizInputOutput io = mock(QuizInputOutput.class);
        MessageSource messageSource = mock(MessageSource.class);
        when(quizValidator.validateNames(any(), eq(""))).thenReturn(java.util.Optional.of("Error: Last name is required. Use --lastname parameter."));
        QuizShellCommands local = new QuizShellCommands(quizService, io, messageSource, Locale.getDefault(), quizValidator);
        String result = local.quiz("John", "");
        assertEquals("Error: Last name is required. Use --lastname parameter.", result);
    }

    @Test
    void testQuizCommandWithNullName() {
        QuizValidator quizValidator = mock(QuizValidator.class);
        QuizService quizService = mock(QuizService.class);
        QuizInputOutput io = mock(QuizInputOutput.class);
        MessageSource messageSource = mock(MessageSource.class);
        when(quizValidator.validateNames(isNull(), any())).thenReturn(java.util.Optional.of("Error: First name is required. Use --name parameter."));
        QuizShellCommands local = new QuizShellCommands(quizService, io, messageSource, Locale.getDefault(), quizValidator);
        String result = local.quiz(null, "Doe");
        assertEquals("Error: First name is required. Use --name parameter.", result);
    }

    @Test
    void testQuizCommandWithNullLastName() {
        QuizValidator quizValidator = mock(QuizValidator.class);
        QuizService quizService = mock(QuizService.class);
        QuizInputOutput io = mock(QuizInputOutput.class);
        MessageSource messageSource = mock(MessageSource.class);
        when(quizValidator.validateNames(any(), isNull())).thenReturn(java.util.Optional.of("Error: Last name is required. Use --lastname parameter."));
        QuizShellCommands local = new QuizShellCommands(quizService, io, messageSource, Locale.getDefault(), quizValidator);
        String result = local.quiz("John", null);
        assertEquals("Error: Last name is required. Use --lastname parameter.", result);
    }

    @Test
    void testQuizCommandWithValidParameters() {
        String result = quizShellCommands.quiz("John", "Doe");

        assertEquals("John Doe, Your score: 2 out of 2", result);
    }

    @Test
    void testQuizCommandWithWhitespaceOnlyName() {
        QuizValidator quizValidator = mock(QuizValidator.class);
        QuizService quizService = mock(QuizService.class);
        QuizInputOutput io = mock(QuizInputOutput.class);
        MessageSource messageSource = mock(MessageSource.class);
        when(quizValidator.validateNames(eq("   "), any())).thenReturn(java.util.Optional.of("Error: First name is required. Use --name parameter."));
        QuizShellCommands local = new QuizShellCommands(quizService, io, messageSource, Locale.getDefault(), quizValidator);
        String result = local.quiz("   ", "Doe");
        assertEquals("Error: First name is required. Use --name parameter.", result);
    }

    @Test
    void testQuizCommandWithWhitespaceOnlyLastName() {
        QuizValidator quizValidator = mock(QuizValidator.class);
        QuizService quizService = mock(QuizService.class);
        QuizInputOutput io = mock(QuizInputOutput.class);
        MessageSource messageSource = mock(MessageSource.class);
        when(quizValidator.validateNames(any(), eq("   "))).thenReturn(java.util.Optional.of("Error: Last name is required. Use --lastname parameter."));
        QuizShellCommands local = new QuizShellCommands(quizService, io, messageSource, Locale.getDefault(), quizValidator);
        String result = local.quiz("John", "   ");
        assertEquals("Error: Last name is required. Use --lastname parameter.", result);
    }
}
