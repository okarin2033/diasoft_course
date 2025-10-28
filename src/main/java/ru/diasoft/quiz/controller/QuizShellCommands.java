package ru.diasoft.quiz.controller;

import org.springframework.context.MessageSource;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.diasoft.quiz.io.QuizInputOutput;
import ru.diasoft.quiz.model.QuizResult;
import ru.diasoft.quiz.service.QuizService;
import ru.diasoft.quiz.service.QuizValidator;

import java.util.Locale;

@ShellComponent
public class QuizShellCommands {

    private final QuizService quizService;
    private final QuizInputOutput io;
    private final MessageSource messageSource;
    private final Locale locale;
    private final QuizValidator quizValidator;

    public QuizShellCommands(QuizService quizService, QuizInputOutput io, MessageSource messageSource, Locale locale, QuizValidator quizValidator) {
        this.quizService = quizService;
        this.io = io;
        this.messageSource = messageSource;
        this.locale = locale;
        this.quizValidator = quizValidator;
    }

    @ShellMethod(key = {"quiz", "q"}, value = "Start a quiz")
    public String quiz(
            @ShellOption(value = {"-n", "--name"}, help = "Your first name") String firstName,
            @ShellOption(value = {"-l", "--lastname"}, help = "Your last name") String lastName) {

        var validationError = quizValidator.validateNames(firstName, lastName);
        if (validationError.isPresent()) return validationError.get();

        QuizResult result = quizService.runQuiz(firstName, lastName, io);
        return result.getResultMessage();
    }

    @ShellMethod(key = {"quiz-interactive", "qi"}, value = "Start a quiz with interactive name input")
    public String quizInteractive() {
        String firstName = io.readNonEmpty(messageSource.getMessage("quiz.enter.name", null, locale));
        String lastName = io.readNonEmpty(messageSource.getMessage("quiz.enter.lastname", null, locale));

        var validationError = quizValidator.validateNames(firstName, lastName);
        if (validationError.isPresent()) return validationError.get();

        QuizResult result = quizService.runQuiz(firstName, lastName, io);
        return result.getResultMessage();
    }

    @ShellMethod(key = {"show-questions", "sq"}, value = "Show all available questions")
    public String showQuestions() {
        return quizService.formatAllQuestions();
    }

    @ShellMethod(key = {"stats", "st"}, value = "Show quiz statistics")
    public String stats() {
        return quizService.formatStats();
    }
}


