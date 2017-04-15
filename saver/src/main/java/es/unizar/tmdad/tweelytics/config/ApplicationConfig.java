package es.unizar.tmdad.tweelytics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.unizar.tmdad.tweelytics.entities.TweetSaver;

@Configuration
public class ApplicationConfig {
	
	@Bean
	public TweetSaver tweetSaver(){
		return new TweetSaver();
	}
}
