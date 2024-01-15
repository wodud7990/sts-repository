package kr.top2blue.boot3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@Slf4j
public class Boot3SecurityEx05Application {

	public static void main(String[] args) {
		SpringApplication.run(Boot3SecurityEx05Application.class, args);
	}

	@Bean
	public CommandLineRunner getCommandLineRunner() {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				log.info("웹브라우저를 실행후 http://localhost:8080/ 접속");
			}
		};
	}
}
