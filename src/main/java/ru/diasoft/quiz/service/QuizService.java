package ru.diasoft.quiz.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.diasoft.quiz.io.QuizInputOutput;
import ru.diasoft.quiz.model.Question;
import ru.diasoft.quiz.model.QuizResult;
import ru.diasoft.quiz.dao.QuestionLoader;

import java.util.List;
import java.util.Locale;

@Service
public class QuizService {

    private final QuestionLoader questionLoader;
    private final MessageSource messageSource;
    private final Locale locale;

    public QuizService(QuestionLoader questionLoader, MessageSource messageSource, Locale locale) {
        this.questionLoader = questionLoader;
        this.messageSource = messageSource;
        this.locale = locale;
    }

    public QuizResult runQuiz(String firstName, String lastName, QuizInputOutput io) {
        List<Question> questions = questionLoader.loadQuestions();
        int score = 0;

        io.printLine("Starting quiz for " + firstName + " " + lastName);
        io.printLine("Answer questions by entering the number of your choice.");
        io.printLine();

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            io.printLine("Question " + (i + 1) + ": " + question.getText());

            List<String> options = question.getOptions();
            for (int j = 0; j < options.size(); j++) {
                io.printLine((j + 1) + ") " + options.get(j));
            }

            String prompt = messageSource.getMessage("quiz.enter.answer", null, locale);
            int answer = io.readAnswer(prompt, options.size());
            
            if (answer - 1 == question.getCorrectIndex()) {
                score++;
            }
            io.printLine();
        }

        String resultMessage = firstName + " " + lastName + ", " + 
            messageSource.getMessage("quiz.your.answer", new Object[]{score, questions.size()}, locale);

        return new QuizResult(score, questions.size(), resultMessage);
    }

    public List<Question> getAllQuestions() {
        return questionLoader.loadQuestions();
    }

    public int getQuestionsCount() {
        return questionLoader.loadQuestions().size();
    }

    public String formatAllQuestions() {
        List<Question> questions = questionLoader.loadQuestions();
        StringBuilder result = new StringBuilder();
        result.append("Available questions (").append(questions.size()).append(" total):\n\n");
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            result.append("Question ").append(i + 1).append(": ").append(question.getText()).append("\n");
            List<String> options = question.getOptions();
            for (int j = 0; j < options.size(); j++) {
                String marker = (j == question.getCorrectIndex()) ? " ✓" : "";
                result.append("  ").append((j + 1)).append(") ").append(options.get(j)).append(marker).append("\n");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public String formatStats() {
        return "Quiz contains " + getQuestionsCount() + " questions.";
    }
}
