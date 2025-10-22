package ru.diasoft.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.diasoft.quiz.model.Question;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuizRunnerTest {

    @Mock
    private QuestionLoader mockQuestionLoader;

    private QuizRunner quizRunner;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        quizRunner = new QuizRunner(mockQuestionLoader);
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testRunWithCorrectAnswers() {
        List<Question> questions = List.of(
            new Question("Вопрос 1?", List.of("Ответ 1", "Ответ 2", "Ответ 3"), 0),
            new Question("Вопрос 2?", List.of("Вариант A", "Вариант B", "Вариант C"), 1)
        );
        
        when(mockQuestionLoader.loadQuestions()).thenReturn(questions);
        
        String input = "Иван\nПетров\n1\n2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        quizRunner.run();

        String output = outputStream.toString();
        assertTrue(output.contains("Введите имя:"));
        assertTrue(output.contains("Введите фамилию:"));
        assertTrue(output.contains("Вопрос 1: Вопрос 1?"));
        assertTrue(output.contains("Иван Петров, ваша оценка: 2 из 2"));
    }

    @Test
    void testRunWithIncorrectAnswers() {
        List<Question> questions = List.of(
            new Question("Вопрос 1?", List.of("Ответ 1", "Ответ 2"), 0)
        );
        
        when(mockQuestionLoader.loadQuestions()).thenReturn(questions);
        
        String input = "Анна\nСидорова\n2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        quizRunner.run();

        String output = outputStream.toString();
        assertTrue(output.contains("Анна Сидорова, ваша оценка: 0 из 1"));
    }

    @Test
    void testRunWithEmptyQuestionsList() {
        when(mockQuestionLoader.loadQuestions()).thenReturn(List.of());
        
        String input = "Тест\nПользователь\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        quizRunner.run();

        String output = outputStream.toString();
        assertTrue(output.contains("Тест Пользователь, ваша оценка: 0 из 0"));
    }
}
