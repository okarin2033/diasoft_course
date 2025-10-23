package ru.diasoft.quiz.model;

public class QuizResult {
    private final int score;
    private final int totalQuestions;
    private final String resultMessage;

    public QuizResult(int score, int totalQuestions, String resultMessage) {
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.resultMessage = resultMessage;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}

