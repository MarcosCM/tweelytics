package es.unizar.tmdad.tweelytics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.unizar.tmdad.tweelytics.entities.TweetAccess;

@Configuration
public class ApplicationConfig {
	
	@Bean
	public TweetAccess tweetAccess(){
		return new TweetAccess();
	}
}
