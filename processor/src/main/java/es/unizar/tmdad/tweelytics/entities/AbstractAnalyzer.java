package es.unizar.tmdad.tweelytics.entities;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;

public abstract class AbstractAnalyzer implements Analyzer{
	
	protected Map<String, Object> params = new HashMap<String, Object>();
	
	@Override
	public Map<String, Double> singleAnalysis(QueriedTweet queriedTweet){
		return batchAnalysis(Collections.singletonList(queriedTweet)).get(queriedTweet);
	};
	
	@Override
	public abstract Map<QueriedTweet, Map<String, Double>> batchAnalysis(List<QueriedTweet> queriedTweets);
	
	@Override
	public void configAnalyzer(Map<String, Object> params) {
		this.params.putAll(params);
	}
}
