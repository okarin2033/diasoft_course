package ru.diasoft.quiz;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.diasoft.quiz.model.Question;
import ru.diasoft.quiz.dao.QuestionLoader;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

@Service
public class QuizRunner {

	private final QuestionLoader questionLoader;
	private final MessageSource messageSource;
	private final Locale locale;

	public QuizRunner(QuestionLoader questionLoader, MessageSource messageSource, Locale locale) {
		this.questionLoader = questionLoader;
		this.messageSource = messageSource;
		this.locale = locale;
	}

	public String runQuiz(String firstName, String lastName) {
		Scanner scanner = new Scanner(System.in);

		List<Question> questions = questionLoader.loadQuestions();
		int score = 0;
		for (int i = 0; i < questions.size(); i++) {
			Question question = questions.get(i);
			System.out.println();
			System.out.println(messageSource.getMessage("quiz.question.number", new Object[]{i + 1}, locale) + " " + question.getText());
			List<String> options = question.getOptions();
			for (int j = 0; j < options.size(); j++) {
				System.out.println((j + 1) + ") " + options.get(j));
			}
			int answer = readAnswer(scanner, options.size());
			if (answer - 1 == question.getCorrectIndex()) {
				score++;
			}
		}

		scanner.close();
		return firstName + " " + lastName + ", " + messageSource.getMessage("quiz.your.answer", new Object[]{score, questions.size()}, locale);
	}

	private int readAnswer(Scanner scanner, int optionsCount) {
		while (true) {
			System.out.print(messageSource.getMessage("quiz.enter.answer", null, locale) + " ");
			String raw = scanner.nextLine();
			try {
				int chosen = Integer.parseInt(raw.trim());
				if (chosen >= 1 && chosen <= optionsCount) {
					return chosen;
				}
			} catch (NumberFormatException ignored) {
			}
			System.out.println(messageSource.getMessage("quiz.invalid.answer", new Object[]{optionsCount}, locale));
		}
	}
}
