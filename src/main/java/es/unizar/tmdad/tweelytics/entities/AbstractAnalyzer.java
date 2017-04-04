package es.unizar.tmdad.tweelytics.entities;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;

public abstract class AbstractAnalyzer implements Analyzer{
	
	public Map<String, Double> singleAnalysis(QueriedTweet queriedTweet){
		return batchAnalysis(Collections.singletonList(queriedTweet)).get(queriedTweet);
	};
	
	public abstract Map<QueriedTweet, Map<String, Double>> batchAnalysis(List<QueriedTweet> queriedTweets);
}
