package ru.diasoft.quiz;

import ru.diasoft.quiz.model.Question;

import java.util.List;
import java.util.Scanner;

public class QuizRunnerLegacy {

    private final QuestionLoaderLegacy questionLoader;

    public QuizRunnerLegacy(QuestionLoaderLegacy questionLoader) {
        this.questionLoader = questionLoader;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите имя: ");
        String firstName = readNonEmpty(scanner);
        System.out.print("Введите фамилию: ");
        String lastName = readNonEmpty(scanner);

        List<Question> questions = questionLoader.loadQuestions();
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            System.out.println();
            System.out.println("Вопрос " + (i + 1) + ": " + question.getText());
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
        System.out.println(firstName + " " + lastName + ", ваша оценка: " + score + " из " + questions.size());
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
            System.out.print("Повторите ввод: ");
        }
    }

    private int readAnswer(Scanner scanner, int optionsCount) {
        while (true) {
            System.out.print("Ваш ответ (введите номер): ");
            String raw = scanner.nextLine();
            try {
                int chosen = Integer.parseInt(raw.trim());
                if (chosen >= 1 && chosen <= optionsCount) {
                    return chosen;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Введите число от 1 до " + optionsCount + ".");
        }
    }
}
