package es.unizar.tmdad.tweelytics.entities;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import es.unizar.tmdad.tweelytics.repository.AnalyzedTweetRepository;

public class QueryAggregator {

	@Autowired
	private AnalyzedTweetRepository analyzedTweetRepository;
	
	/**
	 * Creates a new query aggregator over the queried tweets repository
	 */
	public QueryAggregator(){
	}
	
	/**
	 * Calculates the aggregated predictions over the query
	 * @param query Query to analyze predictions over
	 * @return Analyzed predictions
	 */
	public Map<String, Float> analyzeQuery(String query){
		return analyzedTweetRepository.analyzeTweetsFromQuery(query);
	}
}
