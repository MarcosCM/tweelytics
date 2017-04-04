package es.unizar.tmdad.tweelytics.entities;

import java.util.List;
import java.util.Map;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;

public interface Analyzer {

	/**
	 * Analyzes/predicts a feature in a queried tweet
	 * @param queriedTweet Tweet being analyzed
	 * @return Result of the predictions
	 */
	public Map<String, Double> singleAnalysis(QueriedTweet queriedTweet);
	
	/**
	 * Analyzes/predicts a feature in a list of queried tweets
	 * @param queriedTweet Tweets being analyzed
	 * @return Result of the predictions
	 */
	public Map<QueriedTweet, Map<String, Double>> batchAnalysis(List<QueriedTweet> queriedTweets);
	
	public String getType();
}
