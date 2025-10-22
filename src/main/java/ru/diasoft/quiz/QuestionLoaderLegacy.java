package ru.diasoft.quiz;

import org.springframework.core.io.Resource;
import ru.diasoft.quiz.model.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QuestionLoaderLegacy {

    private final Resource questionsResource;

    public QuestionLoaderLegacy(Resource questionsResource) {
        this.questionsResource = questionsResource;
    }

    public List<Question> loadQuestions() {
        List<Question> questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(questionsResource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                String[] parts = trimmed.split(";", -1);
                if (parts.length < 5) {
                    continue;
                }
                String text = parts[0].trim();
                List<String> options = List.of(parts[1].trim(), parts[2].trim(), parts[3].trim());
                int correctIndex = parseCorrectIndex(parts[4].trim(), options.size());
                questions.add(new Question(text, options, correctIndex));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read questions.csv", e);
        }
        return questions;
    }

    private int parseCorrectIndex(String value, int optionsCount) {
        try {
            int idx = Integer.parseInt(value);
            int zeroBased = idx - 1;
            if (zeroBased < 0 || zeroBased >= optionsCount) {
                return 0;
            }
            return zeroBased;
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
