package es.unizar.tmdad.tweelytics.config;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.unizar.tmdad.tweelytics.entities.EmotionAnalyzer;
import es.unizar.tmdad.tweelytics.entities.IndicoEmotionAnalyzer;
import es.unizar.tmdad.tweelytics.entities.IndicoPoliticalAnalyzer;
import es.unizar.tmdad.tweelytics.entities.IndicoTwitterEngagementAnalyzer;
import es.unizar.tmdad.tweelytics.entities.MockEmotionAnalyzer;
import es.unizar.tmdad.tweelytics.entities.MockPoliticalAnalyzer;
import es.unizar.tmdad.tweelytics.entities.MockTwitterEngagementAnalyzer;
import es.unizar.tmdad.tweelytics.entities.PoliticalAnalyzer;
import es.unizar.tmdad.tweelytics.entities.TweetSaver;
import es.unizar.tmdad.tweelytics.entities.TwitterEngagementAnalyzer;
import io.indico.api.utils.IndicoException;

@Configuration
public class ApplicationConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
	
	@Value("${indico.apiKey}")
	private String indicoApiKey;
	
	@Value("${analyzers.mock}")
	private String analyzersMock;
	
	@Bean
	public EmotionAnalyzer emotionAnalyzer(){
		if (Boolean.parseBoolean(analyzersMock)) return new MockEmotionAnalyzer();
		else
			try {
				return new IndicoEmotionAnalyzer(indicoApiKey);
			} catch (IndicoException e) {
				logger.info(e.getMessage());
				return null;
			}
	}
	
	@Bean
	public PoliticalAnalyzer politicalAnalyzer(){
		if (Boolean.parseBoolean(analyzersMock)) return new MockPoliticalAnalyzer();
		else
			try {
				return new IndicoPoliticalAnalyzer(indicoApiKey);
			} catch (IndicoException e) {
				logger.info(e.getMessage());
				return null;
			}
	}
	
	@Bean
	public TwitterEngagementAnalyzer twitterEngagementAnalyzer(){
		if (Boolean.parseBoolean(analyzersMock)) return new MockTwitterEngagementAnalyzer();
		else
			try {
				return new IndicoTwitterEngagementAnalyzer(indicoApiKey);
			} catch (IndicoException e) {
				logger.info(e.getMessage());
				return null;
			}
	}
	
	@Bean
	public TweetSaver tweetSaver(){
		return new TweetSaver();
	}
}
