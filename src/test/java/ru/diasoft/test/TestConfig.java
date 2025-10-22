package ru.diasoft.test;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import ru.diasoft.quiz.QuizRunner;

import java.util.Locale;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public QuizRunner quizRunner() {
        return mock(QuizRunner.class);
    }

    @Bean
    @Primary
    public MessageSource messageSource() {
        return mock(MessageSource.class);
    }

    @Bean
    @Primary
    public Locale locale() {
        return mock(Locale.class);
    }
}
