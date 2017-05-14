package es.unizar.tmdad.tweelytics.config;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.unizar.tmdad.tweelytics.entities.Analyzer;
import es.unizar.tmdad.tweelytics.entities.IndicoEmotionAnalyzer;
import es.unizar.tmdad.tweelytics.entities.IndicoPoliticalAnalyzer;
import es.unizar.tmdad.tweelytics.entities.IndicoTwitterEngagementAnalyzer;
import es.unizar.tmdad.tweelytics.entities.MockEmotionAnalyzer;
import es.unizar.tmdad.tweelytics.entities.MockPoliticalAnalyzer;
import es.unizar.tmdad.tweelytics.entities.MockTwitterEngagementAnalyzer;
import es.unizar.tmdad.tweelytics.repository.ConfigsRepository;
import io.indico.api.utils.IndicoException;

@Configuration
public class ApplicationConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
	
	@Autowired
	private ConfigsRepository configsRepository;
	
	@Value("${indico.apiKey}")
	private String indicoApiKey;
	
	@Value("${analyzers.mock}")
	private String analyzersMock;
	
	@Bean
	public Analyzer analyzer(){
		logger.info(System.getProperty("analyzer"));
		// Emotion
		if (System.getProperty("analyzer").equals("Emotion")){
			if (Boolean.parseBoolean(analyzersMock)) return new MockEmotionAnalyzer().setConfigsRepository(configsRepository);
			else
				try {
					return new IndicoEmotionAnalyzer(indicoApiKey).setConfigsRepository(configsRepository);
				} catch (IndicoException e) {
					logger.info(e.getMessage());
					return null;
				}
		}
		// Political
		else if(System.getProperty("analyzer").equals("PoliticalClass")){
			if (Boolean.parseBoolean(analyzersMock)) return new MockPoliticalAnalyzer().setConfigsRepository(configsRepository);
			else
				try {
					return new IndicoPoliticalAnalyzer(indicoApiKey).setConfigsRepository(configsRepository);
				} catch (IndicoException e) {
					logger.info(e.getMessage());
					return null;
				}
		}
		// Twitter Engagement
		else{
			if (Boolean.parseBoolean(analyzersMock)) return new MockTwitterEngagementAnalyzer().setConfigsRepository(configsRepository);
			else
				try {
					return new IndicoTwitterEngagementAnalyzer(indicoApiKey).setConfigsRepository(configsRepository);
				} catch (IndicoException e) {
					logger.info(e.getMessage());
					return null;
				}
		}
	}
}
