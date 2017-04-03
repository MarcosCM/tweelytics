package es.unizar.tmdad.tweelytics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.unizar.tmdad.tweelytics.domain.QueryAggregator;
import es.unizar.tmdad.tweelytics.domain.TextAnalyzer;
import io.indico.api.utils.IndicoException;

@Configuration
public class ApplicationConfig {
	
	public static final String SENTIMENT_API = 		"sentiment";
	public static final String SENTIMENT_HQ_API = 	"sentiment_hq";
	public static final String POLITICAL_API = 		"political";
	public static final String PLACES_API =			"places";
	public static final String EMOTION_API =		"emotion";
	
	@Value("${indico.apiKey}")
	private String apiKey;
	
	@Bean
	public TextAnalyzer textAnalyzer() throws IndicoException{
		return new TextAnalyzer(apiKey);
	}
	
	@Bean
	public QueryAggregator queryAggregator(){
		return new QueryAggregator();
	}
}
