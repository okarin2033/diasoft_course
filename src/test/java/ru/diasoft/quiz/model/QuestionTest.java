package ru.diasoft.quiz.model;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    @Test
    void testQuestionCreation() {
        String text = "Какой язык программирования используется в Spring Boot?";
        List<String> options = List.of("Java", "Python", "JavaScript");
        int correctIndex = 0;

        Question question = new Question(text, options, correctIndex);

        assertEquals(text, question.getText());
        assertEquals(options, question.getOptions());
        assertEquals(correctIndex, question.getCorrectIndex());
    }

    @Test
    void testQuestionWithDifferentCorrectIndex() {
        String text = "Какой фреймворк для веб-разработки?";
        List<String> options = List.of("React", "Angular", "Vue");
        int correctIndex = 1;

        Question question = new Question(text, options, correctIndex);

        assertEquals("Angular", question.getOptions().get(correctIndex));
        assertEquals(1, question.getCorrectIndex());
    }
}
