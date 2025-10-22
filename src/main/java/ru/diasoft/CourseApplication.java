package ru.diasoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import ru.diasoft.quiz.QuizRunnerLegacy;

@SpringBootApplication
@ImportResource("classpath:applicationContext.xml")
public class CourseApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(CourseApplication.class, args);
		
		QuizRunnerLegacy quizRunner = context.getBean("quizRunner", QuizRunnerLegacy.class);
		quizRunner.run();
		
		context.close();
	}
}
