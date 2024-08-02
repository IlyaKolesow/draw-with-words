package com.game.draw;

import com.game.draw.util.Unsplash;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DrawWithWordsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DrawWithWordsApplication.class, args);
	}

	@Bean
	public HttpHeaders httpHeaders() {
		return new HttpHeaders();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
