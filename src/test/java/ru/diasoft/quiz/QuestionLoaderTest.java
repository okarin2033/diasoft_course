package ru.diasoft.quiz;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import ru.diasoft.quiz.model.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionLoaderTest {

	@Test
	void loadsQuestionsFromCsv() {
		QuestionLoader loader = new QuestionLoader(new ClassPathResource("questions.csv"));
		List<Question> questions = loader.loadQuestions();
		assertThat(questions).hasSize(5);
		Question first = questions.get(0);
		assertThat(first.getText()).isEqualTo("Capital of France?");
		assertThat(first.getOptions()).containsExactly("Paris", "London", "Berlin");
		assertThat(first.getCorrectIndex()).isEqualTo(0);
	}
}


