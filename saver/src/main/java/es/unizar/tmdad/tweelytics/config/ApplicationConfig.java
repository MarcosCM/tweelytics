package es.unizar.tmdad.tweelytics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.unizar.tmdad.tweelytics.entities.MockTweetSaver;
import es.unizar.tmdad.tweelytics.entities.RepositoryTweetSaver;
import es.unizar.tmdad.tweelytics.entities.TweetSaver;

@Configuration
public class ApplicationConfig {
	
	@Value("${saver.mock}")
	private String saverMock;
	
	@Bean
	public TweetSaver repositoryTweetSaver(){
		if (Boolean.parseBoolean(saverMock)) return new MockTweetSaver();
		else return new RepositoryTweetSaver();
	}
}
