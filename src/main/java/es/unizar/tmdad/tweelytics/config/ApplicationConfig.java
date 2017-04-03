package es.unizar.tmdad.tweelytics.config;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.unizar.tmdad.tweelytics.entities.IndicoTextAnalyzer;
import es.unizar.tmdad.tweelytics.entities.MockTextAnalyzer;
import es.unizar.tmdad.tweelytics.entities.QueryAggregator;
import es.unizar.tmdad.tweelytics.entities.TextAnalyzer;
import io.indico.api.utils.IndicoException;

@Configuration
public class ApplicationConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
	
	@Value("${indico.apiKey}")
	private String indicoApiKey;
	
	@Value("${textAnalyzer.mock}")
	private String textAnalyzerMock;
	
	@Bean
	public TextAnalyzer textAnalyzer(){
		if (Boolean.parseBoolean(textAnalyzerMock)) return new MockTextAnalyzer();
		else
			try {
				return new IndicoTextAnalyzer(indicoApiKey);
			} catch (IndicoException e) {
				logger.info(e.getMessage());
				return null;
			}
	}
	
	@Bean
	public QueryAggregator queryAggregator(){
		return new QueryAggregator();
	}
}
