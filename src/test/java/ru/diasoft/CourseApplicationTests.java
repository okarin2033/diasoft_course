package ru.diasoft;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import ru.diasoft.test.TestConfig;

@SpringBootTest
@Import(TestConfig.class)
@TestPropertySource(properties = {
    "spring.main.web-application-type=none",
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration"
})
class CourseApplicationTests {

	@Test
	void contextLoads() {
	}
}
