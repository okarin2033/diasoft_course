package ru.diasoft.quiz;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.diasoft.quiz.model.Question;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

@Component
public class QuizRunner implements CommandLineRunner {

	private final QuestionLoader questionLoader;
	private final MessageSource messageSource;
	private final Locale locale;

	public QuizRunner(QuestionLoader questionLoader, MessageSource messageSource, Locale locale) {
		this.questionLoader = questionLoader;
		this.messageSource = messageSource;
		this.locale = locale;
	}

	@Override
	public void run(String... args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print(messageSource.getMessage("quiz.enter.name", null, locale));
		String firstName = readNonEmpty(scanner);
		System.out.print(messageSource.getMessage("quiz.enter.lastname", null, locale));
		String lastName = readNonEmpty(scanner);

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

		System.out.println();
		System.out.println(firstName + " " + lastName + ", " + messageSource.getMessage("quiz.your.answer", new Object[]{score, questions.size()}, locale));
	}

	private String readNonEmpty(Scanner scanner) {
		while (true) {
			String value = scanner.nextLine();
			if (value != null) {
				String trimmed = value.trim();
				if (!trimmed.isEmpty()) {
					return trimmed;
				}
			}
			System.out.print(messageSource.getMessage("quiz.repeat.input", null, locale) + " ");
		}
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



