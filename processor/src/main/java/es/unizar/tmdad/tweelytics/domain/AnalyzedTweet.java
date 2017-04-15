package es.unizar.tmdad.tweelytics.domain;

import java.util.Map;

public class AnalyzedTweet {

	private QueriedTweet queriedTweet;
	private Map<String, Double> analyticsResults;
	private String analyzedBy;
	
	public AnalyzedTweet(){
		
	}

	public String getOriginalText() {
		return queriedTweet.getOriginalText();
	}

	public QueriedTweet getQueriedTweet() {
		return queriedTweet;
	}

	public void setQueriedTweet(QueriedTweet queriedTweet) {
		this.queriedTweet = queriedTweet;
	}

	public Map<String, Double> getAnalyticsResults() {
		return analyticsResults;
	}

	public void setAnalyticsResults(Map<String, Double> analyticsResults) {
		this.analyticsResults = analyticsResults;
	}

	public String getAnalyzedBy() {
		return analyzedBy;
	}

	public void setAnalyzedBy(String analyzedBy) {
		this.analyzedBy = analyzedBy;
	}
}
