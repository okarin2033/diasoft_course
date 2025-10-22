package ru.diasoft.quiz.model;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    @Test
    void testQuestionCreation() {
        String text = "Test question?";
        List<String> options = List.of("Option 1", "Option 2", "Option 3");
        int correctIndex = 0;

        Question question = new Question(text, options, correctIndex);

        assertEquals(text, question.getText());
        assertEquals(options, question.getOptions());
        assertEquals(correctIndex, question.getCorrectIndex());
    }
}
