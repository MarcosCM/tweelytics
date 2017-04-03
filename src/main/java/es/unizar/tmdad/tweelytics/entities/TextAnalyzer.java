package es.unizar.tmdad.tweelytics.entities;

import java.util.List;
import java.util.Map;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;

public interface TextAnalyzer {

	/**
	 * Analyzes/predicts the emotions in the text of a queried tweet
	 * @param queriedTweet Tweet being analyzed
	 * @return Result of the predictions
	 */
	public Map<String, Double> singleTextAnalysis(QueriedTweet queriedTweet);
	
	/**
	 * Analyzes/predicts the emotions in the text of a list of queried tweets
	 * @param queriedTweet Tweets being analyzed
	 * @return Result of the predictions
	 */
	public Map<QueriedTweet, Map<String, Double>> batchTextAnalysis(List<QueriedTweet> queriedTweets);
}
