package ru.diasoft.quiz.io;

public interface QuizInputOutput {

    void print(String message);

    void printLine(String message);

    void printLine();

    String readLine();

    String readNonEmpty(String prompt);

    int readAnswer(String prompt, int maxOptions);
}
