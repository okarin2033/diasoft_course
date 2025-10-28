package ru.diasoft.quiz.service;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class QuizValidator {

    public Optional<String> validateNames(String firstName, String lastName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            return Optional.of("Error: First name is required. Use --name parameter.");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            return Optional.of("Error: Last name is required. Use --lastname parameter.");
        }
        return Optional.empty();
    }
}


