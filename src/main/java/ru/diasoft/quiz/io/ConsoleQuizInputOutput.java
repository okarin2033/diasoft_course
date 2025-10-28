package ru.diasoft.quiz.io;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Scanner;

@Component
public class ConsoleQuizInputOutput implements QuizInputOutput {

    private final MessageSource messageSource;
    private final Locale locale;
    private final Scanner scanner;

    public ConsoleQuizInputOutput(MessageSource messageSource, Locale locale) {
        this.messageSource = messageSource;
        this.locale = locale;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void print(String message) {
        System.out.print(message);
    }

    @Override
    public void printLine(String message) {
        System.out.println(message);
    }

    @Override
    public void printLine() {
        System.out.println();
    }

    @Override
    public String readLine() {
        return scanner.nextLine();
    }

    @Override
    public String readNonEmpty(String prompt) {
        while (true) {
            print(prompt);
            String value = readLine();
            if (value != null) {
                String trimmed = value.trim();
                if (!trimmed.isEmpty()) {
                    return trimmed;
                }
            }
            print(messageSource.getMessage("quiz.repeat.input", null, locale) + " ");
        }
    }

    @Override
    public int readAnswer(String prompt, int maxOptions) {
        while (true) {
            print(prompt + " ");
            String raw = readLine();
            try {
                int chosen = Integer.parseInt(raw.trim());
                if (chosen >= 1 && chosen <= maxOptions) {
                    return chosen;
                }
            } catch (NumberFormatException ignored) {
            }
            printLine(messageSource.getMessage("quiz.invalid.answer", new Object[]{maxOptions}, locale));
        }
    }
}
