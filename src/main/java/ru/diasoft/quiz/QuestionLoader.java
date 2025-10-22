package ru.diasoft.quiz;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.diasoft.quiz.model.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class QuestionLoader {

	private final Resource questionsResource;
	private final MessageSource messageSource;

	public QuestionLoader(@Value("classpath:questions.csv") Resource questionsResource, MessageSource messageSource) {
		this.questionsResource = questionsResource;
		this.messageSource = messageSource;
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
			throw new IllegalStateException(messageSource.getMessage("quiz.failed.read.questions", null,
                    Locale.getDefault()), e);
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



